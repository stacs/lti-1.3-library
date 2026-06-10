# LTI 1.3 Library

This project contains a number of classes that can help with ease of development when creating a LTI 1.3 Tool in Java using Spring Boot.

## Configuring client registration

An implementing tool declares its LTI client registration in its own `application.yaml` using the
standard Spring Boot OAuth2 client properties (`spring.security.oauth2.client.*`). Spring Boot's
OAuth2 client auto-configuration reads these properties and builds an
`InMemoryClientRegistrationRepository` for you — there are no beans to define and no extra
configuration code to write.

The sections below walk through a full end-to-end Canvas integration: creating the keys in Canvas,
collecting the values Canvas gives you, and placing them in `application.yaml`. A complete,
ready-to-adapt template combining everything below — both keys, the datasource, and the library
settings — lives at [`tool-example-application.yaml`](tool-example-application.yaml) in the repo
root.

### Using environment variables

Every value in `application.yaml` can be sourced from an environment variable (or any other Spring
property source) with a `${...}` placeholder, so secrets like the Client ID and Secret never need to
be committed to the file:

```yaml
client-id: ${LTI_CLIENT_ID}                 # whole value from an env var
client-secret: ${API_CLIENT_SECRET:unused}  # env var with a fallback default
```

Placeholders can be mixed with literal text and with each other in a single value — useful for
building URLs from a host variable:

```yaml
authorization-uri: https://${CANVAS_HOST:sso.canvaslms.com}/api/lti/authorize_redirect
```

Notes:

- `${VAR:default}` supplies a fallback; without a default, an unset variable fails startup with a
  placeholder-resolution error, so give optional values a default.
- Spring relaxed binding maps an env var to a dotted property by uppercasing and replacing `.`/`-`
  with `_` — e.g. `${canvas.apiUrl}` resolves from `CANVAS_APIURL`, `${api.clientSecret}` from
  `API_CLIENTSECRET`.

### What the library already serves

Before creating the Canvas key, note the two endpoints this library exposes for you (paths are
relative to your tool's base URL / context path):

- **`/.well-known/jwks.json`** — your tool's public JWK set (`JwksController`). Canvas uses this to
  verify messages signed by your tool. This is the **Public JWK URL** you give Canvas.
- **`/config.json`** — a ready-made LTI tool configuration document (`ConfigController`, backed by
  the `canvas-json-config` library). You can hand this URL to a Canvas admin so the developer key
  fields are filled in automatically instead of by hand.

> The bundled `JwksController` generates an in-memory keypair on startup and logs a warning — it is
> only suitable for local development. In production, supply a stable keypair.

## Step 1 — Create the LTI Developer Key in Canvas

The LTI key is what enables LTI 1.3 launches into your tool.

1. Sign in to Canvas as an administrator and open **Admin → Developer Keys**.
2. Click **+ Developer Key → + LTI Key**.
3. Fill in the configuration. The fastest path is **Method → Enter URL** and point it at your
   tool's `https://<your-tool-host>/<context-path>/config.json`; Canvas will pull the settings from
   there. To enter the key fields manually instead, the important ones are:
   - **Title** / **Description** — shown to admins.
   - **Target Link URI** — where Canvas sends a successful launch (your tool's launch entry point).
   - **OpenID Connect Initiation Url** — your tool's OIDC login-initiation endpoint.
   - **Redirect URIs** — must include your tool's login callback, e.g.
     `https://<your-tool-host>/<context-path>/lti/login`. This must match the `redirect-uri` in
     `application.yaml` (see Step 2).
   - **JWK Method → Public JWK URL** — `https://<your-tool-host>/<context-path>/.well-known/jwks.json`.
4. **Save**, then set the key's state to **On**.
5. Copy the **Client ID** — the long numeric key id shown in the Details column (e.g.
   `10000000000123`). This is the value you place in `application.yaml`.
6. Deploy the tool (**Settings → Apps**, or via the key) at the account or course level. Canvas
   then issues a **Deployment ID**; record it if your tool needs to distinguish deployments.

> LTI 1.3 keys do **not** give you a client secret — your tool proves its identity with the keypair
> behind its Public JWK URL, not a shared secret. The `client-secret` in `application.yaml` is only
> a placeholder Spring requires.

### Canvas platform endpoints

These are the same for every cloud-hosted Canvas instance and go in the `provider` block:

| Purpose                 | URL                                                       |
| ----------------------- | --------------------------------------------------------- |
| Authorization (OIDC)    | `https://sso.canvaslms.com/api/lti/authorize_redirect`    |
| Token                   | `https://sso.canvaslms.com/login/oauth2/token`            |
| JWKS (Canvas's keys)    | `https://sso.canvaslms.com/api/lti/security/jwks`         |

For a self-hosted Canvas, substitute your Canvas domain for `sso.canvaslms.com`. The older
`canvas.instructure.com` domain still works but is being phased out; prefer `sso.canvaslms.com`.

## Step 2 — Configure the LTI registration in `application.yaml`

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          canvas:
            client-id: <client-id-from-canvas>   # the LTI key's Client ID from Step 1
            client-secret: unused                 # placeholder; not used by LTI 1.3
            authorization-grant-type: implicit
            scope: openid
            redirect-uri: "{baseUrl}/lti/login"
        provider:
          canvas:
            authorization-uri: https://sso.canvaslms.com/api/lti/authorize_redirect
            token-uri: https://sso.canvaslms.com/login/oauth2/token
            jwk-set-uri: https://sso.canvaslms.com/api/lti/security/jwks
            user-name-attribute: sub
```

### Notes

- **The registration id (`canvas` above)** is the identifier your tool references in its LTI
  security configuration / OIDC initiation (e.g. `/oauth2/authorization/canvas`). The registration
  and provider blocks must use the same id. Keep it simple — avoid dots (`.`) in the id, since
  Spring's relaxed property binding treats them as nested keys.
- **`authorization-grant-type: implicit`** — Spring Security 6 no longer ships the implicit grant
  natively, so the [`spring-security-lti13`](https://github.com/oxctl/spring-security-lti13)
  library supplies `LTIAuthorizationGrantType.IMPLICIT` (internally an authorization-code grant).
  The string value `implicit` binds to it automatically.
- **`redirect-uri: "{baseUrl}/lti/login"`** — `{baseUrl}` is expanded at request time to
  `scheme://host:port` plus the servlet context path, so you do not need to concatenate
  `server.servlet.context-path` manually. It must resolve to the same URL you listed under
  **Redirect URIs** in the Canvas key.
- **`scope: openid`** and **`user-name-attribute: sub`** are required for LTI 1.3.
- **Do not set `issuer-uri`.** Spring Boot performs OIDC discovery against any configured
  `issuer-uri` (fetching `<issuer>/.well-known/openid-configuration`), which Canvas does not serve,
  so the application would fail to start. Configure the `authorization-uri`, `token-uri`, and
  `jwk-set-uri` explicitly instead (as above). `spring-security-lti13`'s `OidcTokenValidator` only
  enforces the `iss` claim when an issuer is present, so it is safely skipped; token signatures are
  still verified against the platform-specific `jwk-set-uri`.

### Where each Canvas value goes

| Value from Canvas                         | `application.yaml` property                                   |
| ----------------------------------------- | ------------------------------------------------------------- |
| LTI key **Client ID**                     | `registration.canvas.client-id`                               |
| (no secret for LTI keys)                  | `registration.canvas.client-secret` → `unused` placeholder    |
| Canvas OIDC authorization endpoint        | `provider.canvas.authorization-uri`                           |
| Canvas token endpoint                     | `provider.canvas.token-uri`                                   |
| Canvas JWKS endpoint                      | `provider.canvas.jwk-set-uri`                                 |

## Step 3 — Canvas REST API access via an API Developer Key

In addition to the LTI key, this version of the library **requires** an API Developer Key so the
tool can call the **Canvas REST API** (e.g. `GET /api/v1/courses`) on behalf of a user. (The LTI key
on its own covers launches and LTI Advantage services — Names & Roles, Assignment & Grades — which
authenticate using that same Client ID and your tool's keypair, but not the general REST API.)

1. In Canvas, open **Admin → Developer Keys → + Developer Key → + API Key**.
2. Set:
   - **Redirect URIs** — your tool's OAuth2 callback (e.g.
     `https://<your-tool-host>/<context-path>/login/oauth2/code/canvas-api`).
   - **Scopes** — enable *Enforce Scopes* and select the Canvas API scopes your tool needs, or leave
     unenforced for full access.
3. **Save** and set the key to **On**.
4. Copy both the **Client ID** and the **Client Secret** from the Details column. Unlike the LTI
   key, an API key *does* have a usable secret.

Configure it as a second registration. API OAuth happens on your institution's Canvas domain (where
the user logs in), not on `sso.canvaslms.com`:

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          canvas-api:
            client-id: <api-key-client-id>
            client-secret: <api-key-client-secret>
            authorization-grant-type: authorization_code
            redirect-uri: "{baseUrl}/login/oauth2/code/canvas-api"
            # scope: optional — list the Canvas API scopes if the key enforces scopes
        provider:
          canvas-api:
            authorization-uri: https://<your-institution>.instructure.com/login/oauth2/auth
            token-uri: https://<your-institution>.instructure.com/login/oauth2/token
```

This library handles LTI launches; the actual Canvas REST API calls are your tool's responsibility.
Spring's OAuth2 client support supplies and refreshes the access token for the `canvas-api`
registration so your tool can attach it to outbound Canvas API requests.

### Where each Canvas value goes

| Value from Canvas              | `application.yaml` property                |
| ------------------------------ | ------------------------------------------ |
| API key **Client ID**          | `registration.canvas-api.client-id`        |
| API key **Client Secret**      | `registration.canvas-api.client-secret`    |
| Canvas `/login/oauth2/auth`    | `provider.canvas-api.authorization-uri`    |
| Canvas `/login/oauth2/token`   | `provider.canvas-api.token-uri`            |
