@import kh.edu.istad.iam.domain.result.IdentityResult

@param java.util.Map<String, String> messages
@param java.util.List<IdentityResult> identities
@param String csrfToken
@param String csrfParameterName
@param boolean googleEnabled
@param boolean githubEnabled
@param boolean facebookEnabled

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>${messages.get("profile.linkedAccounts")}</title>
</head>
<body>
    <main>
        <h1>${messages.get("profile.linkedAccounts")}</h1>

        <ul>
            @for(var identity : identities)
                <li>
                    ${identity.provider()} — ${identity.email()}

                    <form method="post" action="/profile/accounts/${identity.id()}/unlink">
                        <input type="hidden" name="${csrfParameterName}" value="${csrfToken}">
                        <button>${messages.get("common.unlink")}</button>
                    </form>
                </li>
            @endfor
        </ul>

        <p>${messages.get("profile.linkInstructions")}</p>

        <p>
            @if (googleEnabled)
                <a href="/oauth2/authorization/google">${messages.get("provider.google")}</a> ·
            @endif

            @if (githubEnabled)
                <a href="/oauth2/authorization/github">${messages.get("provider.github")}</a> ·
            @endif

            @if (facebookEnabled)
                <a href="/oauth2/authorization/facebook">${messages.get("provider.facebook")}</a>
            @endif
        </p>

        <h2>${messages.get("profile.telegram")}</h2>

        <p>${messages.get("profile.telegramEmailNote")}</p>

        <label>
            ${messages.get("common.email")}
            <input id="telegram-email" type="email">
        </label>

        <!-- Replace the bot username with your Telegram bot's public username. -->
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
            <a href="?lang=en">${messages.get("common.english")}</a>
            |
            <a href="?lang=km">${messages.get("common.khmer")}</a>
        </p>
    </main>
</body>
</html>