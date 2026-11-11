@param java.util.Map<String, String> messages
@param String csrfToken
@param String csrfParameterName

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>${messages.get("profile.changePassword")}</title>
</head>
<body>
    <main>
        <h1>${messages.get("profile.changePassword")}</h1>

        <form method="post" action="/profile/password">
            <input type="hidden" name="${csrfParameterName}" value="${csrfToken}">

            <label>
                ${messages.get("common.currentPassword")}
                <input type="password" name="currentPassword" required>
            </label>

            <label>
                ${messages.get("common.newPassword")}
                <input type="password" name="newPassword" required>
            </label>

            <label>
                ${messages.get("common.confirmPassword")}
                <input type="password" name="confirmPassword" required>
            </label>

            <button>${messages.get("common.submit")}</button>
        </form>

        <p>
            <a href="?lang=en">${messages.get("common.english")}</a>
            |
            <a href="?lang=km">${messages.get("common.khmer")}</a>
        </p>
    </main>
</body>
</html>