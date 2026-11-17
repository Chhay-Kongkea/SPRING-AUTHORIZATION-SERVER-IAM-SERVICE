@param kh.edu.istad.iam.web.i18n.Messages messages
@param String botUsername

@if (botUsername != null && !botUsername.isBlank())
    <h2>${messages.get("profile.telegram")}</h2>
    <p>${messages.get("profile.telegramEmailNote")}</p>
    <label>
        ${messages.get("common.email")}
        <input id="telegram-email" type="email">
    </label>
    <script async src="https://telegram.org/js/telegram-widget.js?22"
            data-telegram-login="${botUsername}"
            data-size="large"
            data-onauth="onTelegramAuth(user)"
            data-request-access="write"></script>
    <script>
        function onTelegramAuth(user) {
            const params = new URLSearchParams(user);
            const email = document.getElementById('telegram-email').value;
            if (email) { params.set('email', email); }
            window.location.href = '/auth/telegram/callback?' + params.toString();
        }
    </script>
@endif
