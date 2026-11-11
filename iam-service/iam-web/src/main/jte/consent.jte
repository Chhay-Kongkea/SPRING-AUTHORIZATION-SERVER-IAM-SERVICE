@param java.util.Map<String, String> messages
@param String clientId
@param String state
@param java.util.List<String> scopes
@param String csrfToken
@param String csrfParameterName

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>${messages.get("consent.title")}</title>
</head>
<body>
    <main>
        <h1>${messages.get("consent.title")}</h1>

        <p>${messages.get("consent.description")}</p>

        <p>
            ${messages.get("consent.requestedScopes")}: ${String.join(", ", scopes)}
        </p>

        <form method="post" action="/oauth2/authorize">
            <input type="hidden" name="${csrfParameterName}" value="${csrfToken}">
            <input type="hidden" name="client_id" value="${clientId}">
            <input type="hidden" name="state" value="${state}">

            @for(var scope : scopes)
                <input type="hidden" name="scope" value="${scope}">
            @endfor

            <button name="user_oauth_approval" value="true">
                ${messages.get("consent.allow")}
            </button>

            <button name="user_oauth_approval" value="false">
                ${messages.get("consent.cancel")}
            </button>
        </form>

        <p>
            <a href="?lang=en">${messages.get("common.english")}</a>
            |
            <a href="?lang=km">${messages.get("common.khmer")}</a>
        </p>
    </main>
</body>
</html>