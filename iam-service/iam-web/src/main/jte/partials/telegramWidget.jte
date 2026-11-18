@param kh.edu.istad.iam.web.i18n.Messages messages
@param String botUsername

@if (botUsername != null && !botUsername.isBlank())
    <div class="telegram-provider">
        <button class="provider-link telegram-provider-link" type="button" id="telegramConnectButton">
            <span class="provider-link-content">
                <span class="provider-icon telegram-logo" aria-hidden="true">
                    <svg viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                        <path fill="currentColor" d="M21.8 3.4 18.5 20c-.3 1.2-1 1.5-2 .9l-5.6-4.1-2.7 2.6c-.3.3-.5.5-1 .5l.4-5.7L18 6.3c.4-.4-.1-.6-.6-.2L6.2 13.2.8 11.5c-1.2-.4-1.2-1.2.2-1.7L21 2.1c.9-.3 1.7.2.8 1.3Z"/>
                    </svg>
                </span>
                <span class="provider-link-name">
                    ${messages.get("login.continueWith", "Telegram")}
                </span>
            </span>
            <span class="provider-link-action">
                ${messages.get("accounts.connect")}
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
                    <path d="M7 17 17 7"/>
                    <path d="M7 7h10v10"/>
                </svg>
            </span>
        </button>
        <div id="telegramWidgetHost" class="telegram-widget-host" hidden></div>
    </div>
    <script>
        (function () {
            const button = document.getElementById("telegramConnectButton");
            const host = document.getElementById("telegramWidgetHost");
            if (!button || !host) return;

            button.addEventListener("click", function () {
                host.hidden = false;
                if (host.dataset.loaded === "true") return;
                const script = document.createElement("script");
                script.async = true;
                script.src = "https://telegram.org/js/telegram-widget.js?22";
                script.dataset.telegramLogin = "${botUsername}";
                script.dataset.size = "large";
                script.dataset.requestAccess = "write";
                script.dataset.onauth = "onTelegramAuth(user)";
                host.appendChild(script);
                host.dataset.loaded = "true";
            });

            window.onTelegramAuth = function (user) {
                const params = new URLSearchParams(user);
                window.location.href = "/auth/telegram/callback?" + params.toString();
            };
        })();
    </script>
@endif
