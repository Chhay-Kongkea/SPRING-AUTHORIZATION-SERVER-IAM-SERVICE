@param java.util.Map<String, String> messages
@param String csrfToken
@param String csrfParameterName
@param String username
@param java.util.List<String> authorities

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>${messages.get("profile.title")}</title>
</head>
<body>
    <main>
        <h1>${messages.get("profile.title")}</h1>

        @if (!username.isBlank())
            <p>${messages.get("profile.username")}: ${username}</p>
            <p>${messages.get("profile.authorities")}: ${String.join(", ", authorities)}</p>
        @endif

        <nav>
            <a href="/profile/edit">${messages.get("profile.edit")}</a>
            ·
            <a href="/profile/password">${messages.get("profile.changePassword")}</a>
            ·
            <a href="/profile/accounts">${messages.get("profile.linkedAccounts")}</a>
            ·
            <a href="/profile/apps">${messages.get("profile.connectedApps")}</a>
        </nav>

        <form method="post" action="/logout">
            <input type="hidden" name="${csrfParameterName}" value="${csrfToken}">
            <button>${messages.get("common.logout")}</button>
        </form>

        <a href="?lang=en">${messages.get("common.english")}</a>
        |
        <a href="?lang=km">${messages.get("common.khmer")}</a>
    </main>
</body>
</html>