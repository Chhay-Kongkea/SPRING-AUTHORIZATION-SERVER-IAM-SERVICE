@param java.util.Map<String, String> messages
@param String errorMessage
@param String csrfToken
@param String csrfParameterName

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>${messages.get("register.title")}</title>
</head>
<body>
    <main>
        <h1>${messages.get("register.title")}</h1>

        @if (errorMessage != null)
            <p role="alert">${errorMessage}</p>
        @endif

        <form method="post" action="/register">
            <input type="hidden" name="${csrfParameterName}" value="${csrfToken}">

            <label>
                ${messages.get("common.username")}
                <input name="username" required minlength="3" maxlength="50">
            </label>

            <label>
                ${messages.get("common.email")}
                <input type="email" name="email" required>
            </label>

            <label>
                ${messages.get("common.givenName")}
                <input name="givenName" required>
            </label>

            <label>
                ${messages.get("common.familyName")}
                <input name="familyName" required>
            </label>

            <label>
                ${messages.get("common.password")}
                <input type="password" name="password" required minlength="12">
            </label>

            <label>
                ${messages.get("common.confirmPassword")}
                <input type="password" name="confirmPassword" required>
            </label>

            <input type="hidden" name="locale" value="en">

            <button>${messages.get("common.submit")}</button>
        </form>

        <a href="/login">${messages.get("common.backToLogin")}</a>

        <p>
            <a href="?lang=en">${messages.get("common.english")}</a>
            |
            <a href="?lang=km">${messages.get("common.khmer")}</a>
        </p>
    </main>
</body>
</html>