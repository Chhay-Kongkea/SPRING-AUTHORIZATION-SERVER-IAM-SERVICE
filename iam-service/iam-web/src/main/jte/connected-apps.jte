@import kh.edu.istad.iam.domain.result.ConnectedApplicationResult

@param java.util.Map<String, String> messages
@param java.util.List<ConnectedApplicationResult> apps
@param String csrfToken
@param String csrfParameterName

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>${messages.get("profile.connectedApps")}</title>
</head>
<body>
    <main>
        <h1>${messages.get("profile.connectedApps")}</h1>

        <ul>
            @for(var app : apps)
                <li>
                    <strong>${app.name()}</strong> — ${app.scopes()}

                    <form method="post" action="/profile/apps/${app.id()}/revoke">
                        <input type="hidden" name="${csrfParameterName}" value="${csrfToken}">
                        <button>${messages.get("common.revoke")}</button>
                    </form>
                </li>
            @endfor
        </ul>

        <p>
            <a href="?lang=en">${messages.get("common.english")}</a>
            |
            <a href="?lang=km">${messages.get("common.khmer")}</a>
        </p>
    </main>
</body>
</html>