@param java.util.Map<String, String> messages
@param String error
@param String csrfToken
@param String csrfParameterName
@param boolean googleEnabled
@param boolean githubEnabled
@param boolean facebookEnabled

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>${messages.get("login.title")}</title>
</head>
<body>
    <main>
        <h1>${messages.get("login.title")}</h1>

        @if (error != null)
            <p role="alert">${messages.get("login.invalidCredentials")}</p>
        @endif

        <form method="post" action="/login">
            <input type="hidden" name="${csrfParameterName}" value="${csrfToken}">

            <label>
                ${messages.get("common.username")}
                <input name="username" required>
            </label>

            <label>
                ${messages.get("common.password")}
                <input type="password" name="password" required>
            </label>

            <button>${messages.get("common.submit")}</button>
        </form>

        <hr>

        @if (googleEnabled)
            <a href="/oauth2/authorization/google">${messages.get("provider.google")}</a> ·
        @endif

        @if (githubEnabled)
            <a href="/oauth2/authorization/github">${messages.get("provider.github")}</a> ·
        @endif

        @if (facebookEnabled)
            <a href="/oauth2/authorization/facebook">${messages.get("provider.facebook")}</a>
        @endif

        <h2>${messages.get("profile.telegram")}</h2>

        <p>${messages.get("profile.telegramEmailNote")}</p>

        <label>
            ${messages.get("common.email")}
            <input id="telegram-email" type="email" required>
        </label>

        <script
            async
            src="https://telegram.org/js/telegram-widget.js?22"
            data-telegram-login="YOUR_TELEGRAM_BOT_USERNAME"
            data-size="large"
            data-onauth="onTelegramAuth(user)"
            data-request-access="write">
        </script>

        <script>
            function onTelegramAuth(user) {
                const email = document.getElementById('telegram-email').value;

                if (!email) {
                    alert('${messages.get("profile.telegramEmailRequired")}');
                    return;
                }

                const params = new URLSearchParams({...user, email});
                window.location.href = '/auth/telegram/callback?' + params.toString();
            }
        </script>

        <p>
            <a href="/register">${messages.get("register.title")}</a>
        </p>

        <a href="?lang=en">${messages.get("common.english")}</a>
        |
        <a href="?lang=km">${messages.get("common.khmer")}</a>
    </main>
</body>
</html>