@param kh.edu.istad.iam.web.i18n.Messages messages
@param String language
@param boolean error
@param boolean loggedOut
@param boolean registered
@param String csrfToken
@param String csrfParameterName
@param boolean googleEnabled
@param boolean githubEnabled
@param boolean facebookEnabled
@param String telegramBotUsername

<!doctype html>
<html lang="${language}">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="theme-color" content="#6d28d9">

<title>${messages.get("login.title")} | ${messages.get("brand.name")}</title>

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
            font-size: 23px;
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
           RIGHT SIDE - LOGIN PANEL
        ======================================== */

        .auth-panel {
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 45px 30px;
            background: #ffffff;
        }

        .login-card {
            width: 100%;
            max-width: 440px;
        }

        .login-header {
            margin-bottom: 34px;
        }

        .login-header h1 {
            font-size: 34px;
            font-weight: 800;
            letter-spacing: -1.2px;
            margin-bottom: 11px;
        }

        .login-header p {
            color: var(--muted);
            font-size: 14px;
            line-height: 1.7;
        }

        /* ========================================
           ALERT MESSAGES
        ======================================== */

        .alert {
            padding: 13px 15px;
            margin-bottom: 18px;
            border-radius: 10px;
            font-size: 13px;
            line-height: 1.6;
        }

        .alert-error {
            color: #b42318;
            background: #fef3f2;
            border: 1px solid #fecdca;
        }

        .alert-success {
            color: #027a48;
            background: #ecfdf3;
            border: 1px solid #abefc6;
        }

        /* ========================================
           LOGIN FORM
        ======================================== */

        .login-form {
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

        .form-group input {
            width: 100%;
            height: 51px;
            padding: 0 16px;
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

        .form-group input:focus {
            border-color: var(--primary);
            background: #ffffff;
            box-shadow: 0 0 0 4px rgba(124, 58, 237, 0.1);
        }

        .form-group input::placeholder {
            color: #aaa5b7;
        }

        /* ========================================
           PASSWORD VISIBILITY
        ======================================== */

        .password-wrapper {
            position: relative;
            width: 100%;
        }

        .password-wrapper input {
            padding-right: 52px;
        }

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

        /* Ensure hidden SVG icons are actually hidden. */
        .password-toggle svg[hidden] {
            display: none !important;
        }

        .password-toggle:focus-visible {
            outline: 2px solid var(--primary);
            outline-offset: 2px;
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
           DIVIDER
        ======================================== */

        .divider {
            display: flex;
            align-items: center;
            gap: 15px;
            margin: 27px 0;
            color: #aaa5b7;
            font-size: 12px;
            font-weight: 600;
        }

        .divider::before,
        .divider::after {
            content: "";
            flex: 1;
            height: 1px;
            background: var(--border);
        }

        /* ========================================
           SOCIAL LOGIN
        ======================================== */

        .social-login {
            display: flex;
            flex-direction: column;
            gap: 11px;
        }

        .social-login a {
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 11px;
            min-height: 47px;
            padding: 12px 15px;
            border: 1px solid var(--border);
            border-radius: 10px;
            color: #454056;
            background: #ffffff;
            text-decoration: none;
            font-size: 13px;
            font-weight: 600;
            transition: all 0.2s ease;
        }

        .social-login a:hover {
            border-color: var(--primary);
            background: #faf8ff;
            color: var(--primary);
            transform: translateY(-1px);
        }

        .social-login a:active {
            transform: translateY(0);
        }

        .social-login svg {
            width: 19px;
            height: 19px;
            flex-shrink: 0;
        }

        /* ========================================
           FOOTER LINKS
        ======================================== */

        .register-link {
            margin-top: 25px;
            text-align: center;
            font-size: 13px;
            color: var(--muted);
        }

        .register-link a,
        .language-section a {
            color: var(--primary);
            font-weight: 700;
            text-decoration: none;
        }

        .register-link a:hover,
        .language-section a:hover {
            text-decoration: underline;
        }

        .language-section {
            margin-top: 22px;
            text-align: center;
            font-size: 13px;
        }

        /* ========================================
           TELEGRAM
        ======================================== */

        .telegram-section {
            margin-top: 20px;
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

            .login-header h1 {
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

            .login-header h1 {
                font-size: 26px;
            }
        }
        /* RentalHub-inspired two-panel auth shell. Form actions and localization stay unchanged. */
        :root {
            --primary: #7c3aed;
            --primary-dark: #6d28d9;
            --primary-light: #ede9fe;
            --text: #17233d;
            --muted: #718096;
            --border: #e5e9f0;
            --background: #f5f7fb;
        }

        body { background: #f8f7fc; }

        .auth-container {
            width: min(1440px, 100%);
            margin: 0 auto;
            padding: 24px;
            gap: 24px;
            background: #f5f7fb;
        }

        .auth-banner {
            min-height: calc(100vh - 48px);
            padding: 42px 46px;
            border-radius: 28px;
            background: linear-gradient(145deg, #4c1d95 0%, #6d28d9 45%, #9333ea 100%);
            box-shadow: 0 22px 55px rgba(23, 35, 61, 0.16);
        }

        .auth-banner::before,
        .auth-banner::after { background: rgba(255, 255, 255, 0.055); }

        .brand-icon {
            background: var(--primary);
            border-color: transparent;
            box-shadow: 0 9px 20px rgba(124, 58, 237, 0.3);
        }

        .banner-content { padding: 45px 0; }

        .banner-label {
            color: #ffd0c5;
            background: rgba(124, 58, 237, 0.17);
            border-color: rgba(252, 165, 165, 0.3);
        }

        .auth-panel {
            min-height: calc(100vh - 48px);
            padding: 54px clamp(28px, 5vw, 76px);
            border-radius: 28px;
            box-shadow: 0 22px 55px rgba(23, 35, 61, 0.08);
        }

        .form-group input {
            min-height: 52px;
            border-radius: 12px;
            border-color: #dce2eb;
            background: #fbfcfe;
        }

        .form-group input:focus {
            border-color: var(--primary);
            box-shadow: 0 0 0 4px rgba(124, 58, 237, 0.12);
            background: #fff;
        }

        .submit-btn {
            min-height: 52px;
            border-radius: 12px;
            background: var(--primary);
            box-shadow: 0 10px 22px rgba(124, 58, 237, 0.22);
        }

        .submit-btn:hover {
            background: var(--primary-dark);
            box-shadow: 0 12px 25px rgba(124, 58, 237, 0.3);
        }

        .social-login a {
            border-radius: 12px;
            border-color: #e0e5ed;
            color: var(--text);
        }

        .social-login a:hover {
            border-color: var(--primary);
            background: var(--primary-light);
        }

        .register-link a,
        .language-section a { color: var(--primary-dark); }

        @media (max-width: 900px) {
            .auth-container {
                grid-template-columns: 1fr;
                padding: 16px;
                gap: 16px;
            }

            .auth-banner,
            .auth-panel {
                min-height: auto;
                border-radius: 22px;
            }

            .auth-banner { padding: 30px; }
            .banner-content { margin: 45px 0 25px; padding: 0; }
            .auth-panel { padding: 42px 28px; }
        }

        body { font-size: 16px; }
        .banner-content p { font-size: 16px; }
        .login-header p,
        .form-group label,
        .social-login a,
        .register-link,
        .language-section,
        .telegram-auth-panel p,
        .telegram-auth-panel label { font-size: 14px; }

        .telegram-section { margin-top: 18px; }

        .telegram-trigger {
            width: 100%;
            min-height: 52px;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 10px;
            padding: 0 16px;
            border: 1px solid #dfe5ed;
            border-radius: 12px;
            background: #fff;
            color: #17233d;
            cursor: pointer;
            font-size: 14px;
            font-weight: 700;
            transition: border-color .2s ease, background .2s ease, transform .2s ease;
        }

        .telegram-trigger svg { width: 20px; height: 20px; color: #229ed9; }

        .telegram-trigger:hover {
            border-color: #229ed9;
            background: #f0f9ff;
            transform: translateY(-1px);
        }

        .telegram-auth-panel {
            display: grid;
            gap: 9px;
            margin-top: 12px;
            padding: 16px;
            border: 1px solid #e5e9f0;
            border-radius: 14px;
            background: #f8fafc;
        }

        .telegram-auth-panel p { margin: 0 0 2px; color: #718096; line-height: 1.55; }
        .telegram-auth-panel label { color: #17233d; font-weight: 700; }
        .telegram-auth-panel input {
            min-height: 48px;
            padding: 0 13px;
            border: 1px solid #dce2eb;
            border-radius: 10px;
            background: #fff;
        }

        .telegram-auth-panel input:focus {
            outline: none;
            border-color: #229ed9;
            box-shadow: 0 0 0 4px rgba(34, 158, 217, .12);
        }

        .telegram-widget { min-height: 44px; margin-top: 4px; }
    </style>
</head>

<body>

<div class="auth-container">

    <!-- ======================================
         LEFT SIDE: BRANDING
    ======================================= -->

    <section class="auth-banner">

        <div class="brand">
            <span>${messages.get("brand.name")}</span>
        </div>

        <div class="banner-content">

            <h2>${messages.get("login.welcomeTitle")}</h2>

            <p>
                ${messages.get("login.welcomeText")}
            </p>
        </div>

        <div class="banner-footer">
            © 2026 ${messages.get("brand.name")}. ${messages.get("common.allRightsReserved")}
        </div>

    </section>


    <section class="auth-panel">

        <main class="login-card">

            <header class="login-header">
                <h1>${messages.get("login.title")}</h1>

                <p>
                    ${messages.get("login.credentialsHint")}
                </p>
            </header>

            <!-- ALERT MESSAGES -->

            @if (error)
                <p class="alert alert-error" role="alert">
                    ${messages.get("login.invalidCredentials")}
                </p>
            @endif

            @if (loggedOut)
                <p class="alert alert-success" role="status">
                    ${messages.get("login.loggedOut")}
                </p>
            @endif

            @if (registered)
                <p class="alert alert-success" role="status">
                    ${messages.get("login.registered")}
                </p>
            @endif

            <!-- LOGIN FORM -->

            <form class="login-form" method="post" action="/login">

                <input
                        type="hidden"
                        name="${csrfParameterName}"
                        value="${csrfToken}">

                <!-- USERNAME -->

                <div class="form-group">

                    <label for="username">
                        ${messages.get("common.usernameOrEmail")}
                    </label>

                    <input
                            id="username"
                            name="username"
                            type="text"
                            placeholder="${messages.get("login.usernamePlaceholder")}"
                            autocomplete="username"
                            required>

                </div>

                <!-- PASSWORD -->

                <div class="form-group">

                    <label for="password">
                        ${messages.get("common.password")}
                    </label>

                    <div class="password-wrapper">

                        <input
                                id="password"
                                type="password"
                                name="password"
                                placeholder="${messages.get("login.passwordPlaceholder")}"
                                autocomplete="current-password"
                                required>

                        <button
                                class="password-toggle"
                                type="button"
                                id="passwordToggle"
                                aria-label="${messages.get("login.showPassword")}"
                                aria-pressed="false">

                            <!-- CLOSED EYE: PASSWORD HIDDEN -->

                            <svg
                                    id="eyeOffIcon"
                                    xmlns="http://www.w3.org/2000/svg"
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

                            <!-- OPEN EYE: PASSWORD VISIBLE -->

                            <svg
                                    id="eyeIcon"
                                    xmlns="http://www.w3.org/2000/svg"
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

                        </button>

                    </div>

                </div>

                <!-- SUBMIT -->

                <button class="submit-btn" type="submit">
                    ${messages.get("login.signIn")}
                </button>

            </form>

            <!-- SOCIAL LOGIN -->

            @if (googleEnabled || githubEnabled || facebookEnabled)

                <div class="divider">
                    ${messages.get("login.orContinueWith")}
                </div>

                <div class="social-login">

                    <!-- GOOGLE -->

                    @if (googleEnabled)
                        <a href="/oauth2/authorization/google">

                            <svg
                                    viewBox="0 0 48 48"
                                    xmlns="http://www.w3.org/2000/svg"
                                    aria-hidden="true">

                                <path fill="#EA4335" d="M24 9.5c3.5 0 6.6 1.2 9.1 3.6l6.8-6.8C35.8 2.4 30.4 0 24 0 14.6 0 6.5 5.4 2.6 13.2l7.9 6.1C12.4 13.1 17.7 9.5 24 9.5Z"/>

                                <path fill="#4285F4" d="M46.1 24.5c0-1.6-.2-3.1-.5-4.5H24v9h12.4c-.6 3-2.3 5.5-4.8 7.2l7.5 5.8c4.4-4.1 7-10.1 7-17.5Z"/>

                                <path fill="#FBBC05" d="M10.5 28.7a14.5 14.5 0 0 1 0-9.4l-7.9-6.1a24 24 0 0 0 0 21.6l7.9-6.1Z"/>

                                <path fill="#34A853" d="M24 48c6.5 0 12-2.1 16-6l-7.5-5.8c-2.1 1.4-4.8 2.3-8.5 2.3-6.3 0-11.6-3.6-13.5-8.8l-7.9 6.1C6.5 43.6 14.6 48 24 48Z"/>

                            </svg>

                            ${messages.get("login.continueWith", "Google")}

                        </a>
                    @endif

                    <!-- GITHUB -->

                    @if (githubEnabled)
                        <a href="/oauth2/authorization/github">

                            <svg
                                    viewBox="0 0 24 24"
                                    fill="currentColor"
                                    xmlns="http://www.w3.org/2000/svg"
                                    aria-hidden="true">

                                <path d="M12 .9a11.1 11.1 0 0 0-3.5 21.6c.6.1.8-.2.8-.6v-2.1c-3.3.7-4-.8-4.3-1.5-.1-.4-.8-1.5-1.3-1.8-.4-.2-1-.8 0-.8.9 0 1.6.9 1.9 1.3 1.1 1.8 2.8 1.3 3.5 1 .1-.8.4-1.3.7-1.6-2.9-.3-5.9-1.5-5.9-6.4 0-1.4.5-2.5 1.3-3.4-.1-.3-.6-1.7.1-3.4 0 0 1-.3 3.5 1.3a12.1 12.1 0 0 1 6.4 0c2.5-1.7 3.5-1.3 3.5-1.3.7 1.7.3 3.1.1 3.4.8.9 1.3 2 1.3 3.4 0 4.9-3 6.1-5.9 6.4.5.4.8 1.1.8 2.2v3.3c0 .4.2.7.8.6A11.1 11.1 0 0 0 12 .9Z"/>

                            </svg>

                            ${messages.get("login.continueWith", "GitHub")}

                        </a>
                    @endif

                    <!-- FACEBOOK -->

                    @if (facebookEnabled)
                        <a href="/oauth2/authorization/facebook">

                            <svg
                                    viewBox="0 0 24 24"
                                    xmlns="http://www.w3.org/2000/svg"
                                    aria-hidden="true">

                                <path fill="#1877F2" d="M24 12a12 12 0 1 0-13.9 11.9v-8.4H7.1V12h3v-2.7c0-3 1.8-4.7 4.5-4.7 1.3 0 2.6.2 2.6.2v2.9h-1.5c-1.5 0-2 .9-2 1.9V12H17l-.5 3.5h-2.8v8.4A12 12 0 0 0 24 12Z"/>

                                <path fill="#fff" d="M16.7 15.5 17.2 12h-3.4v-2.4c0-1 .5-1.9 2-1.9h1.5V4.8s-1.3-.2-2.6-.2c-2.7 0-4.5 1.7-4.5 4.7V12h-3v3.5h3v8.4a12.5 12.5 0 0 0 3.6 0v-8.4h2.9Z"/>

                            </svg>

                            ${messages.get("login.continueWith", "Facebook")}

                        </a>
                    @endif

                </div>

            @endif

            <!-- TELEGRAM LOGIN -->

            @if (telegramBotUsername != null && !telegramBotUsername.isBlank())
                <div class="telegram-section">
                    <button class="telegram-trigger" type="button" id="telegramTrigger" aria-expanded="false">
                        <svg viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
                            <path fill="currentColor" d="M21.8 3.4 18.5 20c-.3 1.2-1 1.5-2 .9l-5.6-4.1-2.7 2.6c-.3.3-.5.5-1 .5l.4-5.7L18 6.3c.4-.4-.1-.6-.6-.2L6.2 13.2.8 11.5c-1.2-.4-1.2-1.2.2-1.7L21 2.1c.9-.3 1.7.2.8 1.3Z"/>
                        </svg>
                        <span>${messages.get("login.continueWith", "Telegram")}</span>
                    </button>


                </div>
            @endif

            <!-- REGISTER -->

            <div class="register-link">

                ${messages.get("login.registerPrompt")}

                <a href="/register">
                    ${messages.get("login.register")}
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

        function initializePasswordToggle() {

            const passwordInput =
                document.getElementById("password");

            const passwordToggle =
                document.getElementById("passwordToggle");

            const eyeIcon =
                document.getElementById("eyeIcon");

            const eyeOffIcon =
                document.getElementById("eyeOffIcon");

            // Stop if any required element is missing.
            if (
                !passwordInput ||
                !passwordToggle ||
                !eyeIcon ||
                !eyeOffIcon
            ) {
                return;
            }

            // Initial state: password hidden, closed eye displayed.
            passwordInput.type = "password";

            eyeIcon.hidden = true;
            eyeOffIcon.hidden = false;

            passwordToggle.setAttribute(
                "aria-label",
                "${messages.get("login.showPassword")}"
            );

            passwordToggle.setAttribute(
                "aria-pressed",
                "false"
            );

            // Prevent duplicate event listeners.
            if (passwordToggle.dataset.initialized === "true") {
                return;
            }

            passwordToggle.dataset.initialized = "true";

            passwordToggle.addEventListener("click", function () {

                const showPassword =
                    passwordInput.type === "password";

                // Toggle password visibility.
                passwordInput.type =
                    showPassword ? "text" : "password";

                // Show the correct eye icon.
                if (showPassword) {

                    // Password is visible: show open eye.
                    eyeIcon.hidden = false;
                    eyeOffIcon.hidden = true;

                } else {

                    // Password is hidden: show closed eye.
                    eyeIcon.hidden = true;
                    eyeOffIcon.hidden = false;

                }

                // Update accessibility attributes.
                passwordToggle.setAttribute(
                    "aria-label",
                    showPassword ? "${messages.get("login.hidePassword")}" : "${messages.get("login.showPassword")}"
                );

                passwordToggle.setAttribute(
                    "aria-pressed",
                    String(showPassword)
                );

            });
        }

        // Initialize after the document is ready.
        if (document.readyState === "loading") {

            document.addEventListener(
                "DOMContentLoaded",
                initializePasswordToggle
            );

        } else {

            initializePasswordToggle();

        }

    })();
</script>

@if (telegramBotUsername != null && !telegramBotUsername.isBlank())
    <script>
        (function () {
            const trigger = document.getElementById("telegramTrigger");
            const panel = document.getElementById("telegramAuthPanel");
            const widgetHost = document.getElementById("telegramWidget");

            if (!trigger || !panel || !widgetHost) return;

            trigger.addEventListener("click", function () {
                panel.hidden = false;
                trigger.setAttribute("aria-expanded", "true");

                if (widgetHost.dataset.loaded === "true") return;
                const script = document.createElement("script");
                script.async = true;
                script.src = "https://telegram.org/js/telegram-widget.js?22";
                script.dataset.telegramLogin = "${telegramBotUsername}";
                script.dataset.size = "large";
                script.dataset.requestAccess = "write";
                script.dataset.onauth = "onTelegramAuth(user)";
                widgetHost.appendChild(script);
                widgetHost.dataset.loaded = "true";
            });

            window.onTelegramAuth = function (user) {
                const params = new URLSearchParams(user);
                window.location.href = "/auth/telegram/callback?" + params.toString();
            };
        })();
    </script>
@endif

</body>
</html>
