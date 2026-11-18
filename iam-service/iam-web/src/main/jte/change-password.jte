@param kh.edu.istad.iam.web.i18n.Messages messages
@param String language
@param String errorKey
@param String csrfToken
@param String csrfParameterName

<!doctype html>
<html lang="${language}">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="theme-color" content="#6d28d9">

<title>${messages.get("profile.changePassword")} | ${messages.get("brand.name")}</title>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>

    <link
            href="https://fonts.googleapis.com/css2?family=Noto+Sans:wght@400;500;600;700;800&display=swap"
            rel="stylesheet">

    <style>
        :root {
            --primary: #7c3aed;
            --primary-dark: #6d28d9;
            --primary-light: #ede9fe;
            --text: #1e1b2e;
            --muted: #77738a;
            --border: #e8e5ef;
            --background: #f8f7fc;
        }

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            min-height: 100vh;
            font-family: "Noto Sans", sans-serif;
            background: var(--background);
            color: var(--text);
            -webkit-font-smoothing: antialiased;
        }

        button,
        input {
            font: inherit;
        }

        .auth-container {
            min-height: 100vh;
            display: grid;
            grid-template-columns: 1fr 1fr;
        }

        /* ========================================
           LEFT SIDE - BRANDING
        ======================================== */

        .auth-banner {
            position: relative;
            overflow: hidden;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            padding: 55px 65px;
            color: white;
            background: linear-gradient(
                    145deg,
                    #4c1d95 0%,
                    #6d28d9 45%,
                    #9333ea 100%
            );
        }

        .auth-banner::before,
        .auth-banner::after {
            content: "";
            position: absolute;
            border-radius: 50%;
            background: rgba(255, 255, 255, 0.08);
            pointer-events: none;
        }

        .auth-banner::before {
            width: 450px;
            height: 450px;
            top: -150px;
            right: -150px;
        }

        .auth-banner::after {
            width: 350px;
            height: 350px;
            bottom: -120px;
            left: -120px;
        }

        .brand {
            position: relative;
            z-index: 1;
            display: flex;
            align-items: center;
            gap: 12px;
            font-size: 23px;
            font-weight: 800;
            letter-spacing: -0.5px;
        }

        .brand-icon {
            width: 43px;
            height: 43px;
            display: grid;
            place-items: center;
            border-radius: 13px;
            background: rgba(255, 255, 255, 0.18);
            border: 1px solid rgba(255, 255, 255, 0.25);
        }

        .banner-content {
            position: relative;
            z-index: 1;
            max-width: 490px;
            margin: auto 0;
            padding: 65px 0;
        }

        .banner-label {
            display: inline-block;
            padding: 9px 16px;
            margin-bottom: 27px;
            border: 1px solid rgba(255, 255, 255, 0.25);
            border-radius: 30px;
            background: rgba(255, 255, 255, 0.12);
            font-size: 12px;
            font-weight: 700;
            letter-spacing: 1.5px;
            text-transform: uppercase;
        }

        .banner-content h2 {
            font-size: clamp(38px, 4vw, 57px);
            line-height: 1.15;
            letter-spacing: -2px;
            margin-bottom: 24px;
        }

        .banner-content p {
            max-width: 430px;
            color: rgba(255, 255, 255, 0.8);
            font-size: 16px;
            line-height: 1.9;
        }

        .banner-footer {
            position: relative;
            z-index: 1;
            color: rgba(255, 255, 255, 0.65);
            font-size: 12px;
        }

        /* ========================================
           RIGHT SIDE - FORM PANEL
        ======================================== */

        .auth-panel {
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 45px 30px;
            background: #ffffff;
        }

        .form-card {
            width: 100%;
            max-width: 440px;
        }

        .form-header {
            margin-bottom: 30px;
        }

        .form-header h1 {
            font-size: 34px;
            font-weight: 800;
            letter-spacing: -1.2px;
            margin-bottom: 11px;
        }

        .form-header p {
            color: var(--muted);
            font-size: 14px;
            line-height: 1.8;
        }

        /* ========================================
           ERROR MESSAGE
        ======================================== */

        .alert {
            padding: 13px 15px;
            margin-bottom: 20px;
            border-radius: 10px;
            font-size: 13px;
            line-height: 1.6;
        }

        .alert-error {
            color: #b42318;
            background: #fef3f2;
            border: 1px solid #fecdca;
        }

        /* ========================================
           PASSWORD FORM
        ======================================== */

        .password-form {
            display: flex;
            flex-direction: column;
            gap: 21px;
        }

        .form-group {
            display: flex;
            flex-direction: column;
            gap: 9px;
        }

        .form-group label {
            font-size: 13px;
            font-weight: 700;
            color: #343047;
        }

        .password-wrapper {
            position: relative;
            width: 100%;
        }

        .password-wrapper input {
            width: 100%;
            height: 51px;
            padding: 0 52px 0 16px;
            border: 1px solid var(--border);
            border-radius: 11px;
            outline: none;
            background: #faf9fc;
            color: var(--text);
            font-size: 14px;
            transition:
                    border-color 0.2s ease,
                    box-shadow 0.2s ease,
                    background 0.2s ease;
        }

        .password-wrapper input:focus {
            border-color: var(--primary);
            background: #ffffff;
            box-shadow: 0 0 0 4px rgba(124, 58, 237, 0.1);
        }

        .password-wrapper input::placeholder {
            color: #aaa5b7;
        }

        /* ========================================
           PASSWORD VISIBILITY TOGGLE
        ======================================== */

        .password-toggle {
            position: absolute;
            top: 50%;
            right: 12px;
            transform: translateY(-50%);
            z-index: 2;

            display: flex;
            align-items: center;
            justify-content: center;

            width: 32px;
            height: 32px;
            padding: 0;

            border: none;
            border-radius: 7px;
            background: transparent;
            color: #9993a8;
            cursor: pointer;

            transition:
                    color 0.2s ease,
                    background 0.2s ease;
        }

        .password-toggle:hover {
            color: var(--primary);
            background: var(--primary-light);
        }

        .password-toggle svg {
            display: block;
            width: 19px;
            height: 19px;
            flex-shrink: 0;
        }

        .password-toggle svg[hidden] {
            display: none !important;
        }

        .password-toggle:focus-visible {
            outline: 2px solid var(--primary);
            outline-offset: 2px;
        }

        /* ========================================
           PASSWORD REQUIREMENT
        ======================================== */

        .password-hint {
            color: var(--muted);
            font-size: 11px;
            line-height: 1.6;
            margin-top: -3px;
        }

        /* ========================================
           SUBMIT BUTTON
        ======================================== */

        .submit-btn {
            width: 100%;
            height: 52px;
            margin-top: 5px;
            border: none;
            border-radius: 11px;
            background: linear-gradient(
                    135deg,
                    #7c3aed,
                    #9333ea
            );
            color: white;
            font-size: 15px;
            font-weight: 700;
            cursor: pointer;
            box-shadow: 0 7px 18px rgba(124, 58, 237, 0.2);
            transition: all 0.2s ease;
        }

        .submit-btn:hover {
            background: linear-gradient(
                    135deg,
                    #6d28d9,
                    #7e22ce
            );
            transform: translateY(-2px);
            box-shadow: 0 10px 24px rgba(124, 58, 237, 0.3);
        }

        .submit-btn:active {
            transform: translateY(0);
        }

        /* ========================================
           BACK LINK
        ======================================== */

        .back-link {
            margin-top: 25px;
            text-align: center;
            font-size: 13px;
            color: var(--muted);
        }

        .back-link a,
        .language-section a {
            color: var(--primary);
            font-weight: 700;
            text-decoration: none;
        }

        .back-link a:hover,
        .language-section a:hover {
            text-decoration: underline;
        }

        .language-section {
            margin-top: 22px;
            text-align: center;
            font-size: 13px;
        }

        /* ========================================
           ACCESSIBILITY
        ======================================== */

        a:focus-visible,
        button:focus-visible,
        input:focus-visible {
            outline: 3px solid rgba(124, 58, 237, 0.25);
            outline-offset: 2px;
        }

        /* ========================================
           RESPONSIVE
        ======================================== */

        @media (max-width: 900px) {
            .auth-container {
                grid-template-columns: 0.85fr 1.15fr;
            }

            .auth-banner {
                padding: 40px 35px;
            }

            .banner-content h2 {
                font-size: 40px;
            }
        }

        @media (max-width: 700px) {
            .auth-container {
                grid-template-columns: 1fr;
            }

            .auth-banner {
                min-height: 290px;
                padding: 30px 25px;
            }

            .banner-content {
                padding: 40px 0 25px;
            }

            .banner-content h2 {
                font-size: 35px;
            }

            .banner-content p {
                font-size: 14px;
            }

            .auth-panel {
                padding: 45px 24px;
                align-items: flex-start;
            }

            .form-header h1 {
                font-size: 29px;
            }
        }

        @media (max-width: 380px) {
            .auth-banner {
                min-height: 250px;
            }

            .banner-content h2 {
                font-size: 30px;
            }

            .auth-panel {
                padding: 35px 18px;
            }

            .form-header h1 {
                font-size: 26px;
            }
        }
        /* Single-column account page */
        .auth-container { display: block; max-width: 920px; margin: 0 auto; padding: 32px 20px; }
        .auth-banner { min-height: auto; margin-bottom: 24px; padding: 28px 32px; border-radius: 20px; }
        .auth-panel { min-height: auto; padding: 0; background: transparent; box-shadow: none; }
        .form-card { max-width: 720px; margin: 0 auto; }
        .submit-btn:hover, .password-toggle:hover, .back-link a:hover, .language-section a:hover { color: var(--primary-dark); background: var(--primary-light); }
        @media (max-width: 700px) { .auth-container { padding: 20px 14px; } .auth-banner { padding: 24px; } }
    </style>
</head>

<body>

<div class="auth-container">


    <!-- ======================================
         RIGHT SIDE: CHANGE PASSWORD
    ======================================= -->

    <section class="auth-panel">

        <main class="form-card">

            <header class="form-header">

                <h1>
                    ${messages.get("profile.changePassword")}
                </h1>

                <p>
                ${messages.get("password.instructions")}
                </p>

            </header>

            <!-- ERROR MESSAGE -->

            @if (!errorKey.isEmpty())
                <p class="alert alert-error" role="alert">
                    ${messages.get(errorKey)}
                </p>
            @endif

            <!-- CHANGE PASSWORD FORM -->

            <form
                    class="password-form"
                    method="post"
                    action="/profile/password">

                <input
                        type="hidden"
                        name="${csrfParameterName}"
                        value="${csrfToken}">

                <!-- CURRENT PASSWORD -->

                <div class="form-group">

                    <label for="currentPassword">
                        ${messages.get("common.currentPassword")}
                    </label>

                    <div class="password-wrapper">

                        <input
                                id="currentPassword"
                                type="password"
                                name="currentPassword"
                                placeholder="${messages.get("password.currentPlaceholder")}"
                                autocomplete="current-password"
                                required>

                        <button
                                class="password-toggle"
                                type="button"
                                data-password-toggle
                                aria-label="${messages.get("login.showPassword")}"
                                aria-pressed="false">

                            <svg
                                    data-eye-open
                                    viewBox="0 0 24 24"
                                    fill="none"
                                    stroke="currentColor"
                                    stroke-width="1.8"
                                    stroke-linecap="round"
                                    stroke-linejoin="round"
                                    aria-hidden="true"
                                    hidden>
                                <path d="M2 12s3.6-7 10-7 10 7 10 7-3.6 7-10 7-10-7-10-7Z"/>
                                <circle cx="12" cy="12" r="3"/>
                            </svg>

                            <svg
                                    data-eye-closed
                                    viewBox="0 0 24 24"
                                    fill="none"
                                    stroke="currentColor"
                                    stroke-width="1.8"
                                    stroke-linecap="round"
                                    stroke-linejoin="round"
                                    aria-hidden="true">
                                <path d="M3 3 21 21"/>
                                <path d="M10.6 10.6a2 2 0 0 0 2.8 2.8"/>
                                <path d="M9.9 5.2A11 11 0 0 1 12 5c6.4 0 10 7 10 7a16 16 0 0 1-4 4.8"/>
                                <path d="M6.2 6.2C3.5 8 2 12 2 12s3.6 7 10 7c1.3 0 2.5-.3 3.5-.8"/>
                            </svg>

                        </button>

                    </div>

                </div>

                <!-- NEW PASSWORD -->

                <div class="form-group">

                    <label for="newPassword">
                        ${messages.get("common.newPassword")}
                    </label>

                    <div class="password-wrapper">

                        <input
                                id="newPassword"
                                type="password"
                                name="newPassword"
                                placeholder="${messages.get("password.newPlaceholder")}"
                                autocomplete="new-password"
                                required
                                minlength="12">

                        <button
                                class="password-toggle"
                                type="button"
                                data-password-toggle
                                aria-label="${messages.get("login.showPassword")}"
                                aria-pressed="false">

                            <svg
                                    data-eye-open
                                    viewBox="0 0 24 24"
                                    fill="none"
                                    stroke="currentColor"
                                    stroke-width="1.8"
                                    stroke-linecap="round"
                                    stroke-linejoin="round"
                                    aria-hidden="true"
                                    hidden>
                                <path d="M2 12s3.6-7 10-7 10 7 10 7-3.6 7-10 7-10-7-10-7Z"/>
                                <circle cx="12" cy="12" r="3"/>
                            </svg>

                            <svg
                                    data-eye-closed
                                    viewBox="0 0 24 24"
                                    fill="none"
                                    stroke="currentColor"
                                    stroke-width="1.8"
                                    stroke-linecap="round"
                                    stroke-linejoin="round"
                                    aria-hidden="true">
                                <path d="M3 3 21 21"/>
                                <path d="M10.6 10.6a2 2 0 0 0 2.8 2.8"/>
                                <path d="M9.9 5.2A11 11 0 0 1 12 5c6.4 0 10 7 10 7a16 16 0 0 1-4 4.8"/>
                                <path d="M6.2 6.2C3.5 8 2 12 2 12s3.6 7 10 7c1.3 0 2.5-.3 3.5-.8"/>
                            </svg>

                        </button>

                    </div>

                    <p class="password-hint">
                    ${messages.get("password.rule")}
                    </p>

                </div>

                <!-- CONFIRM PASSWORD -->

                <div class="form-group">

                    <label for="confirmPassword">
                        ${messages.get("common.confirmPassword")}
                    </label>

                    <div class="password-wrapper">

                        <input
                                id="confirmPassword"
                                type="password"
                                name="confirmPassword"
                                placeholder="${messages.get("password.confirmPlaceholder")}"
                                autocomplete="new-password"
                                required>

                        <button
                                class="password-toggle"
                                type="button"
                                data-password-toggle
                                aria-label="${messages.get("login.showPassword")}"
                                aria-pressed="false">

                            <svg
                                    data-eye-open
                                    viewBox="0 0 24 24"
                                    fill="none"
                                    stroke="currentColor"
                                    stroke-width="1.8"
                                    stroke-linecap="round"
                                    stroke-linejoin="round"
                                    aria-hidden="true"
                                    hidden>
                                <path d="M2 12s3.6-7 10-7 10 7 10 7-3.6 7-10 7-10-7-10-7Z"/>
                                <circle cx="12" cy="12" r="3"/>
                            </svg>

                            <svg
                                    data-eye-closed
                                    viewBox="0 0 24 24"
                                    fill="none"
                                    stroke="currentColor"
                                    stroke-width="1.8"
                                    stroke-linecap="round"
                                    stroke-linejoin="round"
                                    aria-hidden="true">
                                <path d="M3 3 21 21"/>
                                <path d="M10.6 10.6a2 2 0 0 0 2.8 2.8"/>
                                <path d="M9.9 5.2A11 11 0 0 1 12 5c6.4 0 10 7 10 7a16 16 0 0 1-4 4.8"/>
                                <path d="M6.2 6.2C3.5 8 2 12 2 12s3.6 7 10 7c1.3 0 2.5-.3 3.5-.8"/>
                            </svg>

                        </button>

                    </div>

                </div>

                <!-- SUBMIT -->

                <button class="submit-btn" type="submit">
                    ${messages.get("common.save")}
                </button>

            </form>

            <!-- BACK LINK -->

            <div class="back-link">
                <a href="/">
                    ← ${messages.get("common.back")}
                </a>
            </div>

            <!-- LANGUAGE SWITCHER -->

            <div class="language-section">
                @template.partials.languageSwitcher(messages = messages)
            </div>

        </main>

    </section>

</div>

<!-- ======================================
     PASSWORD VISIBILITY JAVASCRIPT
======================================= -->

<script>
    (function () {

        function initializePasswordToggles() {

            const toggleButtons =
                document.querySelectorAll("[data-password-toggle]");

            toggleButtons.forEach(function (button) {

                if (button.dataset.initialized === "true") {
                    return;
                }

                const wrapper = button.closest(".password-wrapper");

                if (!wrapper) {
                    return;
                }

                const passwordInput =
                    wrapper.querySelector("input");

                const eyeOpen =
                    button.querySelector("[data-eye-open]");

                const eyeClosed =
                    button.querySelector("[data-eye-closed]");

                if (!passwordInput || !eyeOpen || !eyeClosed) {
                    return;
                }

                button.dataset.initialized = "true";

                // Initial state: password hidden.
                passwordInput.type = "password";
                eyeOpen.hidden = true;
                eyeClosed.hidden = false;

                button.setAttribute(
                    "aria-label",
                    "${messages.get("login.showPassword")}"
                );

                button.setAttribute(
                    "aria-pressed",
                    "false"
                );

                button.addEventListener("click", function () {

                    const showPassword =
                        passwordInput.type === "password";

                    passwordInput.type =
                        showPassword ? "text" : "password";

                    eyeOpen.hidden = !showPassword;
                    eyeClosed.hidden = showPassword;

                    button.setAttribute(
                        "aria-label",
                        showPassword ? "${messages.get("login.hidePassword")}" : "${messages.get("login.showPassword")}"
                    );

                    button.setAttribute(
                        "aria-pressed",
                        String(showPassword)
                    );

                });

            });

        }

        if (document.readyState === "loading") {

            document.addEventListener(
                "DOMContentLoaded",
                initializePasswordToggles
            );

        } else {

            initializePasswordToggles();

        }

    })();
</script>

</body>
</html>
