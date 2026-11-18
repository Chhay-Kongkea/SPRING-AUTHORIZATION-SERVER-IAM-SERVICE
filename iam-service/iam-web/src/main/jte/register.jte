
@param kh.edu.istad.iam.web.i18n.Messages messages
@param String language
@param java.util.Map<String, String> values
@param java.util.Map<String, String> errors
@param String csrfToken
@param String csrfParameterName

<!doctype html>
<html lang="${language}">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="theme-color" content="#6d28d9">

    <title>${messages.get("register.title")} | ${messages.get("brand.name")}</title>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>

    <link
            href="https://fonts.googleapis.com/css2?family=Noto+Sans:wght@400;500;600;700;800&display=swap"
            rel="stylesheet">

    <style>
        /* ========================================
           ROOT VARIABLES
        ======================================== */

        :root {
            --primary: #7c3aed;
            --primary-dark: #6d28d9;
            --primary-light: #ede9fe;

            --text: #17233d;
            --muted: #718096;

            --border: #e5e9f0;
            --background: #f5f7fb;

            --danger: #b42318;
            --danger-light: #fef3f2;

            --success: #16a34a;
        }

        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            min-height: 100vh;
            font-family: "Noto Sans", sans-serif;
            font-size: 16px;
            color: var(--text);
            background: var(--background);
            -webkit-font-smoothing: antialiased;
        }

        button,
        input {
            font: inherit;
        }

        /* ========================================
           MAIN AUTHENTICATION LAYOUT
        ======================================== */

        .page {
            width: min(1440px, 100%);
            min-height: 100vh;

            display: grid;
            grid-template-columns: minmax(320px, 0.9fr) minmax(480px, 1.1fr);

            gap: 24px;
            margin: 0 auto;
            padding: 24px;

            background: var(--background);
        }

        /* ========================================
           LEFT SIDE - BRANDING PANEL
           SAME DESIGN AS LOGIN PAGE
        ======================================== */

        .auth-banner {
            position: relative;
            overflow: hidden;

            display: flex;
            flex-direction: column;
            justify-content: space-between;

            min-height: calc(100vh - 48px);

            padding: 42px 46px;

            color: #ffffff;

            border-radius: 28px;

            background: linear-gradient(
                    145deg,
                    #4c1d95 0%,
                    #6d28d9 45%,
                    #9333ea 100%
            );

            box-shadow: 0 22px 55px rgba(23, 35, 61, 0.16);
        }

        /* Decorative circles */

        .auth-banner::before,
        .auth-banner::after {
            content: "";

            position: absolute;

            border-radius: 50%;

            background: rgba(255, 255, 255, 0.055);

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

        /* Brand */

        .brand {
            position: relative;
            z-index: 1;

            display: flex;
            align-items: center;
            gap: 12px;

            font-size: 23px;
            font-weight: 800;
            letter-spacing: -0.5px;

            color: #ffffff;
        }

        /* Branding content */

        .banner-content {
            position: relative;
            z-index: 1;

            max-width: 490px;

            margin: auto 0;
            padding: 45px 0;
        }

        .banner-content h2 {
            margin-bottom: 24px;

            font-size: clamp(38px, 4vw, 57px);
            line-height: 1.15;
            letter-spacing: -2px;
            font-weight: 800;

            color: #ffffff;
        }

        .banner-content p {
            max-width: 430px;

            color: rgba(255, 255, 255, 0.8);

            font-size: 16px;
            line-height: 1.9;
        }

        /* Branding footer */

        .banner-footer {
            position: relative;
            z-index: 1;

            color: rgba(255, 255, 255, 0.65);

            font-size: 12px;
        }

        /* ========================================
           RIGHT SIDE - REGISTER PANEL
        ======================================== */

        .form-panel {
            min-height: calc(100vh - 48px);

            display: flex;
            align-items: center;
            justify-content: center;

            padding: 54px clamp(28px, 5vw, 76px);

            border-radius: 28px;

            background: #ffffff;

            box-shadow: 0 22px 55px rgba(23, 35, 61, 0.08);
        }

        .register-card {
            width: 100%;
            max-width: 470px;

            animation: fade-in 0.5s ease both;
        }

        /* Mobile brand */

        .mobile-logo {
            display: none;
        }

        /* ========================================
           FORM HEADER
        ======================================== */

        .form-heading {
            margin-bottom: 28px;
        }

        .form-heading h1 {
            margin-bottom: 9px;

            font-size: 30px;
            font-weight: 800;
            letter-spacing: -1px;

            color: var(--text);
        }

        .form-heading p {
            color: var(--muted);

            font-size: 14px;
            line-height: 1.7;
        }

        /* ========================================
           FORM ERROR
        ======================================== */

        .form-error {
            display: flex;
            align-items: flex-start;
            gap: 10px;

            margin-bottom: 20px;
            padding: 13px 15px;

            border: 1px solid #fecaca;
            border-radius: 11px;

            background: var(--danger-light);
            color: var(--danger);

            font-size: 13px;
            line-height: 1.6;
        }

        .form-error-icon {
            flex-shrink: 0;
            font-weight: 800;
        }

        /* ========================================
           FORM GRID
        ======================================== */

        .form-grid {
            display: grid;
            grid-template-columns: repeat(2, minmax(0, 1fr));

            gap: 17px 14px;
        }

        .form-group {
            min-width: 0;
        }

        .form-group.full-width {
            grid-column: 1 / -1;
        }

        /* Labels */

        .form-group label {
            display: block;

            margin-bottom: 8px;

            color: #353147;

            font-size: 14px;
            font-weight: 700;
        }

        .required {
            color: var(--danger);
        }

        /* Inputs */

        .input-wrapper {
            position: relative;
        }

        .form-group input {
            width: 100%;
            min-height: 52px;

            padding: 0 14px;

            border: 1px solid #dce2eb;
            border-radius: 12px;

            outline: none;

            background: #fbfcfe;
            color: var(--text);

            font-size: 14px;

            transition:
                    border-color 0.2s ease,
                    box-shadow 0.2s ease,
                    background 0.2s ease;
        }

        .form-group input::placeholder {
            color: #aaa6b7;
        }

        .form-group input:hover {
            border-color: #c4b5fd;
        }

        .form-group input:focus {
            border-color: var(--primary);

            background: #ffffff;

            box-shadow: 0 0 0 4px rgba(124, 58, 237, 0.12);
        }

        .form-group input[aria-invalid="true"] {
            border-color: var(--danger);
        }

        /* Field validation errors */

        .field-error {
            margin: 7px 0 0;

            color: var(--danger);

            font-size: 12px;
            line-height: 1.5;
        }

        /* ========================================
           PASSWORD VISIBILITY
        ======================================== */

        .password-input {
            padding-right: 48px !important;
        }

        .toggle-password {
            position: absolute;

            top: 50%;
            right: 10px;

            width: 32px;
            height: 32px;

            display: grid;
            place-items: center;

            transform: translateY(-50%);

            border: none;
            border-radius: 7px;

            background: transparent;
            color: var(--muted);

            cursor: pointer;

            transition:
                    color 0.2s ease,
                    background 0.2s ease;
        }

        .toggle-password:hover {
            background: var(--primary-light);
            color: var(--primary);
        }

        .toggle-password svg {
            width: 18px;
            height: 18px;
        }

        .password-hint {
            margin: 7px 0 0;

            color: var(--muted);

            font-size: 14px;
            line-height: 1.5;
        }

        /* ========================================
           SUBMIT BUTTON
        ======================================== */

        .submit-button {
            width: 100%;
            min-height: 52px;

            display: flex;
            align-items: center;
            justify-content: center;
            gap: 9px;

            margin-top: 25px;

            border: none;
            border-radius: 12px;

            background: var(--primary);
            color: #ffffff;

            font-size: 14px;
            font-weight: 700;

            cursor: pointer;

            box-shadow: 0 10px 22px rgba(124, 58, 237, 0.22);

            transition:
                    transform 0.2s ease,
                    background 0.2s ease,
                    box-shadow 0.2s ease;
        }

        .submit-button:hover {
            background: var(--primary-dark);

            transform: translateY(-1px);

            box-shadow: 0 12px 25px rgba(124, 58, 237, 0.3);
        }

        .submit-button:active {
            transform: translateY(0);
        }

        /* ========================================
           LOGIN LINK
        ======================================== */

        .login-link {
            margin: 23px 0 0;

            color: var(--muted);

            text-align: center;
            font-size: 14px;
        }

        .login-link a {
            color: var(--primary-dark);

            font-weight: 700;
            text-decoration: none;
        }

        .login-link a:hover {
            text-decoration: underline;
        }

        /* ========================================
           SECURITY NOTICE
        ======================================== */

        .security-note {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 7px;

            margin-top: 24px;

            color: #858196;

            font-size: 14px;
            text-align: center;
        }

        .security-note span {
            color: var(--success);
        }

        /* ========================================
           LANGUAGE SWITCHER
        ======================================== */

        .language-area {
            display: flex;
            justify-content: center;

            margin-top: 22px;
        }

        .language-area a {
            color: var(--primary-dark);
        }

        /* ========================================
           ANIMATION
        ======================================== */

        @keyframes fade-in {
            from {
                opacity: 0;
                transform: translateY(10px);
            }

            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        /* ========================================
           RESPONSIVE - TABLET
        ======================================== */

        @media (max-width: 1000px) {
            .page {
                grid-template-columns: minmax(260px, 0.85fr) minmax(420px, 1.15fr);
                gap: 16px;
                padding: 16px;
            }

            .auth-banner {
                padding: 38px 30px;
                min-height: calc(100vh - 32px);
                border-radius: 22px;
            }

            .banner-content h2 {
                font-size: 40px;
            }

            .form-panel {
                min-height: calc(100vh - 32px);
                padding: 42px 28px;
                border-radius: 22px;
            }
        }

        /* ========================================
           RESPONSIVE - MOBILE
        ======================================== */

        @media (max-width: 700px) {
            .page {
                display: block;

                min-height: 100vh;

                padding: 0;
            }

            /* Hide desktop branding panel */

            .auth-banner {
                display: none;
            }

            .form-panel {
                min-height: 100vh;

                padding: 35px 22px;

                border-radius: 0;

                box-shadow: none;
            }

            .register-card {
                max-width: 460px;
            }

            /* Display mobile brand */

            .mobile-logo {
                display: flex;
                align-items: center;
                justify-content: center;

                gap: 10px;

                margin-bottom: 35px;

                color: var(--primary-dark);

                font-size: 19px;
                font-weight: 800;
                letter-spacing: -0.5px;
            }

            .form-heading h1 {
                font-size: 27px;
            }
        }

        /* ========================================
           RESPONSIVE - SMALL MOBILE
        ======================================== */

        @media (max-width: 420px) {
            .form-grid {
                grid-template-columns: 1fr;
            }

            .form-group.full-width {
                grid-column: auto;
            }

            .form-panel {
                padding: 28px 18px;
            }

            .form-heading h1 {
                font-size: 25px;
            }
        }

        /* ========================================
           ACCESSIBILITY
        ======================================== */

        .submit-button:focus-visible,
        .toggle-password:focus-visible,
        a:focus-visible {
            outline: 3px solid rgba(124, 58, 237, 0.35);
            outline-offset: 3px;
        }

        @media (prefers-reduced-motion: reduce) {
            *,
            *::before,
            *::after {
                scroll-behavior: auto !important;
                animation-duration: 0.01ms !important;
                animation-iteration-count: 1 !important;
                transition-duration: 0.01ms !important;
            }
        }
    </style>
</head>

<body>

<div class="page">

    <!-- ======================================
         LEFT SIDE: BRANDING
         SAME DESIGN AS LOGIN PAGE
    ======================================= -->

    <section class="auth-banner">

        <div class="brand">
            <span>${messages.get("brand.name")}</span>
        </div>

        <div class="banner-content">

            <h2>${messages.get("register.welcomeTitle")}</h2>

            <p>
                ${messages.get("register.welcomeText")}
            </p>

        </div>

        <div class="banner-footer">
            © 2026 ${messages.get("brand.name")}.
            ${messages.get("common.allRightsReserved")}
        </div>

    </section>


    <!-- ======================================
         RIGHT SIDE: REGISTRATION FORM
    ======================================= -->

    <main class="form-panel">

        <div class="register-card">

            <!-- MOBILE BRAND -->

            <div class="mobile-logo">
                <span>${messages.get("brand.name")}</span>
            </div>


            <!-- FORM HEADER -->

            <div class="form-heading">

                <h1>
                    ${messages.get("register.title")}
                </h1>

                <p>
                    ${messages.get("register.instructions")}
                </p>

            </div>


            <!-- ======================================
                 FORM-LEVEL ERROR
            ======================================= -->

            @if (errors.containsKey("form"))

                <div class="form-error" role="alert">

                    <span class="form-error-icon">!</span>

                    <span>
                        ${messages.get(errors.get("form"))}
                    </span>

                </div>

            @endif


            <!-- ======================================
                 REGISTRATION FORM
            ======================================= -->

            <form method="post" action="/register">

                <!-- CSRF TOKEN -->

                <input
                        type="hidden"
                        name="${csrfParameterName}"
                        value="${csrfToken}"
                >


                <div class="form-grid">

                    <!-- ======================================
                         USERNAME
                    ======================================= -->

                    <div class="form-group full-width">

                        <label for="username">
                            ${messages.get("common.username")}
                            <span class="required">*</span>
                        </label>

                        <input
                                id="username"
                                type="text"
                                name="username"
                                value="${values.getOrDefault("username", "")}"
                                placeholder="${messages.get("register.usernamePlaceholder")}"
                                autocomplete="username"
                                minlength="3"
                                maxlength="50"
                                required
                                aria-invalid="${errors.containsKey("username")}"
                        >

                        @if (errors.containsKey("username"))

                            <p class="field-error" role="alert">
                                ${messages.get(errors.get("username"))}
                            </p>

                        @endif

                    </div>


                    <!-- ======================================
                         EMAIL
                    ======================================= -->

                    <div class="form-group full-width">

                        <label for="email">
                            ${messages.get("common.email")}
                            <span class="required">*</span>
                        </label>

                        <input
                                id="email"
                                type="email"
                                name="email"
                                value="${values.getOrDefault("email", "")}"
                                placeholder="${messages.get("register.emailPlaceholder")}"
                                autocomplete="email"
                                required
                                aria-invalid="${errors.containsKey("email")}"
                        >

                        @if (errors.containsKey("email"))

                            <p class="field-error" role="alert">
                                ${messages.get(errors.get("email"))}
                            </p>

                        @endif

                    </div>


                    <!-- ======================================
                         GIVEN NAME
                    ======================================= -->

                    <div class="form-group">

                        <label for="givenName">
                            ${messages.get("common.givenName")}
                            <span class="required">*</span>
                        </label>

                        <input
                                id="givenName"
                                type="text"
                                name="givenName"
                                value="${values.getOrDefault("givenName", "")}"
                                placeholder="${messages.get("register.givenNamePlaceholder")}"
                                autocomplete="given-name"
                                required
                                aria-invalid="${errors.containsKey("givenName")}"
                        >

                        @if (errors.containsKey("givenName"))

                            <p class="field-error" role="alert">
                                ${messages.get(errors.get("givenName"))}
                            </p>

                        @endif

                    </div>


                    <!-- ======================================
                         FAMILY NAME
                    ======================================= -->

                    <div class="form-group">

                        <label for="familyName">
                            ${messages.get("common.familyName")}
                            <span class="required">*</span>
                        </label>

                        <input
                                id="familyName"
                                type="text"
                                name="familyName"
                                value="${values.getOrDefault("familyName", "")}"
                                placeholder="${messages.get("register.familyNamePlaceholder")}"
                                autocomplete="family-name"
                                required
                                aria-invalid="${errors.containsKey("familyName")}"
                        >

                        @if (errors.containsKey("familyName"))

                            <p class="field-error" role="alert">
                                ${messages.get(errors.get("familyName"))}
                            </p>

                        @endif

                    </div>


                    <!-- ======================================
                         PASSWORD
                    ======================================= -->

                    <div class="form-group full-width">

                        <label for="password">
                            ${messages.get("common.password")}
                            <span class="required">*</span>
                        </label>

                        <div class="input-wrapper">

                            <input
                                    id="password"
                                    class="password-input"
                                    type="password"
                                    name="password"
                                    autocomplete="new-password"
                                    placeholder="${messages.get("register.passwordPlaceholder")}"
                                    minlength="12"
                                    required
                                    aria-invalid="${errors.containsKey("password")}"
                            >

                            <button
                                    class="toggle-password"
                                    type="button"
                                    data-password-toggle="password"
                                    aria-label="${messages.get("login.showPassword")}"
                                    aria-pressed="false"
                            >

                                <svg
                                        viewBox="0 0 24 24"
                                        fill="none"
                                        stroke="currentColor"
                                        stroke-width="1.8"
                                        stroke-linecap="round"
                                        stroke-linejoin="round"
                                        aria-hidden="true"
                                >
                                    <path d="M2 12s3.6-7 10-7 10 7 10 7-3.6 7-10 7-10-7-10-7Z"/>
                                    <circle cx="12" cy="12" r="3"/>
                                </svg>

                            </button>

                        </div>


                        <!-- PASSWORD REQUIREMENT -->

                        <p class="password-hint">
                            ${messages.get("register.passwordRule")}
                        </p>


                        @if (errors.containsKey("password"))

                            <p class="field-error" role="alert">
                                ${messages.get(errors.get("password"))}
                            </p>

                        @endif

                    </div>


                    <!-- ======================================
                         CONFIRM PASSWORD
                    ======================================= -->

                    <div class="form-group full-width">

                        <label for="confirmPassword">
                            ${messages.get("common.confirmPassword")}
                            <span class="required">*</span>
                        </label>

                        <div class="input-wrapper">

                            <input
                                    id="confirmPassword"
                                    class="password-input"
                                    type="password"
                                    name="confirmPassword"
                                    autocomplete="new-password"
                                    placeholder="${messages.get("register.confirmPasswordPlaceholder")}"
                                    required
                                    aria-invalid="${errors.containsKey("confirmPassword")}"
                            >

                            <button
                                    class="toggle-password"
                                    type="button"
                                    data-password-toggle="confirmPassword"
                                    aria-label="${messages.get("login.showPassword")}"
                                    aria-pressed="false"
                            >

                                <svg
                                        viewBox="0 0 24 24"
                                        fill="none"
                                        stroke="currentColor"
                                        stroke-width="1.8"
                                        stroke-linecap="round"
                                        stroke-linejoin="round"
                                        aria-hidden="true"
                                >
                                    <path d="M2 12s3.6-7 10-7 10 7 10 7-3.6 7-10 7-10-7-10-7Z"/>
                                    <circle cx="12" cy="12" r="3"/>
                                </svg>

                            </button>

                        </div>


                        @if (errors.containsKey("confirmPassword"))

                            <p class="field-error" role="alert">
                                ${messages.get(errors.get("confirmPassword"))}
                            </p>

                        @endif

                    </div>

                </div>


                <!-- ======================================
                     SUBMIT BUTTON
                ======================================= -->

                <button class="submit-button" type="submit">

                    <span>
                        ${messages.get("register.title")}
                    </span>

                    <span aria-hidden="true">→</span>

                </button>

            </form>


            <!-- ======================================
                 LOGIN LINK
            ======================================= -->

            <p class="login-link">

                ${messages.get("register.haveAccount")}

                <a href="/login">
                    ${messages.get("common.backToLogin")}
                </a>

            </p>


            <!-- ======================================
                 SECURITY NOTICE
            ======================================= -->

            <div class="security-note">

                <span aria-hidden="true">●</span>

                ${messages.get("register.securityNotice")}

            </div>


            <!-- ======================================
                 LANGUAGE SWITCHER
            ======================================= -->

            <div class="language-area">

                @template.partials.languageSwitcher(messages = messages)

            </div>

        </div>

    </main>

</div>


<!-- ======================================
     PASSWORD VISIBILITY JAVASCRIPT
======================================= -->

<script>
    (function () {

        "use strict";

        const passwordToggleButtons =
            document.querySelectorAll("[data-password-toggle]");

        passwordToggleButtons.forEach((button) => {

            button.addEventListener("click", () => {

                const inputId = button.dataset.passwordToggle;

                const input = document.getElementById(inputId);

                if (!input) {
                    return;
                }

                const isVisible = input.type === "text";

                // Toggle password visibility.
                input.type = isVisible ? "password" : "text";

                // Update accessibility state.
                button.setAttribute(
                    "aria-pressed",
                    String(!isVisible)
                );

                // Update accessibility label.
                button.setAttribute(
                    "aria-label",
                    isVisible
                        ? "${messages.get("login.showPassword")}"
                        : "${messages.get("login.hidePassword")}"
                );

            });

        });

    })();
</script>

</body>
</html>
