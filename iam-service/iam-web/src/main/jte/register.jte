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

    <title>${messages.get("register.title")} | IAM Platform</title>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans:wght@400;500;600;700;800&display=swap" rel="stylesheet">

    <style>
        :root {
            --primary: #7c3aed;
            --primary-dark: #6d28d9;
            --primary-light: #ede9fe;
            --text: #1e1b2e;
            --muted: #77738a;
            --border: #e8e5ef;
            --background: #f8f7fc;
            --danger: #dc2626;
            --danger-light: #fef2f2;
            --success: #16a34a;
        }

        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            min-height: 100vh;
            font-family: 'Noto Sans', sans-serif;
            color: var(--text);
            background: var(--background);
        }

        button,
        input {
            font: inherit;
        }

        .page {
            min-height: 100vh;
            display: grid;
            grid-template-columns: minmax(320px, 0.9fr) minmax(480px, 1.1fr);
        }

        /* Branding panel */

        .brand-panel {
            position: relative;
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            overflow: hidden;
            padding: 52px;
            color: #fff;
            background: linear-gradient(
                    145deg,
                    #4c1d95 0%,
                    #6d28d9 45%,
                    #9333ea 100%
            );
        }

        .brand-panel::before,
        .brand-panel::after {
            position: absolute;
            content: "";
            border: 1px solid rgba(255, 255, 255, 0.12);
            border-radius: 50%;
            pointer-events: none;
        }

        .brand-panel::before {
            width: 460px;
            height: 460px;
            top: -170px;
            right: -180px;
        }

        .brand-panel::after {
            width: 350px;
            height: 350px;
            bottom: -150px;
            left: -150px;
        }

        .brand-content,
        .brand-footer {
            position: relative;
            z-index: 1;
        }

        .brand-logo {
            display: inline-flex;
            align-items: center;
            gap: 12px;
            font-size: 19px;
            font-weight: 800;
            letter-spacing: -0.5px;
        }

        .logo-icon {
            width: 42px;
            height: 42px;
            display: grid;
            place-items: center;
            border: 1px solid rgba(255, 255, 255, 0.3);
            border-radius: 13px;
            background: rgba(255, 255, 255, 0.15);
            font-size: 21px;
        }

        .brand-message {
            max-width: 460px;
            margin: 105px 0 0;
        }

        .brand-message .eyebrow {
            display: inline-block;
            margin-bottom: 22px;
            padding: 7px 13px;
            border: 1px solid rgba(255, 255, 255, 0.25);
            border-radius: 30px;
            background: rgba(255, 255, 255, 0.1);
            color: #ede9fe;
            font-size: 12px;
            font-weight: 700;
            letter-spacing: 1px;
            text-transform: uppercase;
        }

        .brand-message h2 {
            margin: 0 0 20px;
            font-size: clamp(34px, 4vw, 48px);
            line-height: 1.2;
            letter-spacing: -1.8px;
        }

        .brand-message p {
            max-width: 390px;
            margin: 0;
            color: rgba(255, 255, 255, 0.78);
            font-size: 15px;
            line-height: 1.9;
        }

        .benefits {
            display: grid;
            gap: 18px;
            margin-top: 42px;
        }

        .benefit {
            display: flex;
            align-items: center;
            gap: 13px;
            color: rgba(255, 255, 255, 0.9);
            font-size: 14px;
        }

        .benefit-icon {
            width: 30px;
            height: 30px;
            display: grid;
            place-items: center;
            flex-shrink: 0;
            border-radius: 9px;
            background: rgba(255, 255, 255, 0.15);
            font-size: 15px;
        }

        .brand-footer {
            margin-top: 60px;
            color: rgba(255, 255, 255, 0.65);
            font-size: 12px;
        }

        /* Form panel */

        .form-panel {
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 48px 35px;
            background: #fff;
        }

        .register-card {
            width: 100%;
            max-width: 470px;
            animation: fade-in 0.5s ease both;
        }

        .mobile-logo {
            display: none;
        }

        .form-heading {
            margin-bottom: 28px;
        }

        .form-heading h1 {
            margin: 0 0 9px;
            font-size: 30px;
            font-weight: 800;
            letter-spacing: -1px;
        }

        .form-heading p {
            margin: 0;
            color: var(--muted);
            font-size: 14px;
            line-height: 1.7;
        }

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

        .form-group label {
            display: block;
            margin-bottom: 8px;
            color: #353147;
            font-size: 13px;
            font-weight: 700;
        }

        .required {
            color: var(--danger);
        }

        .input-wrapper {
            position: relative;
        }

        .form-group input {
            width: 100%;
            height: 48px;
            padding: 0 14px;
            border: 1px solid var(--border);
            border-radius: 10px;
            outline: none;
            background: #fff;
            color: var(--text);
            font-size: 13px;
            transition: border-color 0.2s, box-shadow 0.2s;
        }

        .form-group input::placeholder {
            color: #aaa6b7;
        }

        .form-group input:hover {
            border-color: #c4b5fd;
        }

        .form-group input:focus {
            border-color: var(--primary);
            box-shadow: 0 0 0 4px rgba(124, 58, 237, 0.1);
        }

        .form-group input[aria-invalid="true"] {
            border-color: var(--danger);
        }

        .field-error {
            margin: 7px 0 0;
            color: var(--danger);
            font-size: 12px;
            line-height: 1.5;
        }

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
            border: 0;
            border-radius: 7px;
            background: transparent;
            color: var(--muted);
            cursor: pointer;
        }

        .toggle-password:hover {
            background: var(--primary-light);
            color: var(--primary);
        }

        .password-hint {
            margin: 7px 0 0;
            color: var(--muted);
            font-size: 11px;
            line-height: 1.5;
        }

        .submit-button {
            width: 100%;
            height: 50px;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 9px;
            margin-top: 25px;
            border: 0;
            border-radius: 10px;
            background: linear-gradient(135deg, var(--primary), var(--primary-dark));
            color: #fff;
            font-size: 14px;
            font-weight: 700;
            cursor: pointer;
            box-shadow: 0 7px 16px rgba(109, 40, 217, 0.2);
            transition: transform 0.2s, box-shadow 0.2s;
        }

        .submit-button:hover {
            transform: translateY(-1px);
            box-shadow: 0 10px 22px rgba(109, 40, 217, 0.28);
        }

        .submit-button:active {
            transform: translateY(0);
        }

        .submit-button:focus-visible,
        .toggle-password:focus-visible,
        a:focus-visible {
            outline: 3px solid rgba(124, 58, 237, 0.35);
            outline-offset: 3px;
        }

        .login-link {
            margin: 23px 0 0;
            color: var(--muted);
            text-align: center;
            font-size: 13px;
        }

        .login-link a {
            color: var(--primary);
            font-weight: 700;
            text-decoration: none;
        }

        .login-link a:hover {
            text-decoration: underline;
        }

        .security-note {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 7px;
            margin-top: 24px;
            color: #858196;
            font-size: 11px;
        }

        .security-note span {
            color: var(--success);
        }

        .language-area {
            display: flex;
            justify-content: center;
            margin-top: 22px;
        }

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

        @media (max-width: 900px) {
            .page {
                grid-template-columns: minmax(260px, 0.75fr) minmax(450px, 1.25fr);
            }

            .brand-panel {
                padding: 38px 30px;
            }

            .form-panel {
                padding: 40px 25px;
            }

            .brand-message h2 {
                font-size: 36px;
            }
        }

        @media (max-width: 700px) {
            .page {
                display: block;
            }

            .brand-panel {
                display: none;
            }

            .form-panel {
                min-height: 100vh;
                padding: 35px 22px;
            }

            .register-card {
                max-width: 460px;
            }

            .mobile-logo {
                display: flex;
                align-items: center;
                justify-content: center;
                gap: 10px;
                margin-bottom: 35px;
                color: var(--primary-dark);
                font-size: 17px;
                font-weight: 800;
            }

            .mobile-logo .logo-icon {
                border-color: var(--primary-light);
                background: var(--primary-light);
                color: var(--primary);
            }

            .form-heading h1 {
                font-size: 27px;
            }
        }

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

    <!-- Branding panel -->
    <aside class="brand-panel">
        <div class="brand-content">
            <div class="brand-logo">
                <span class="logo-icon">✦</span>
                <span>IAM Platform</span>
            </div>

            <div class="brand-message">
                <span class="eyebrow">Create your account</span>

                <h2>Your identity.<br>One secure place.</h2>

                <p>
                    Create your account and get started with a secure,
                    centralized identity experience.
                </p>

                <div class="benefits">
                    <div class="benefit">
                        <span class="benefit-icon">✓</span>
                        <span>Secure account management</span>
                    </div>

                    <div class="benefit">
                        <span class="benefit-icon">✓</span>
                        <span>One identity across applications</span>
                    </div>

                    <div class="benefit">
                        <span class="benefit-icon">✓</span>
                        <span>Privacy-focused authentication</span>
                    </div>
                </div>
            </div>
        </div>

        <div class="brand-footer">
            © 2026 IAM Platform. All rights reserved.
        </div>
    </aside>

    <!-- Registration form -->
    <main class="form-panel">
        <div class="register-card">

            <div class="mobile-logo">
                <span class="logo-icon">✦</span>
                <span>IAM Platform</span>
            </div>

            <div class="form-heading">
                <h1>${messages.get("register.title")}</h1>
                <p>Create your account by completing the information below.</p>
            </div>

            @if (errors.containsKey("form"))
                <div class="form-error" role="alert">
                    <span class="form-error-icon">!</span>
                    <span>${messages.get(errors.get("form"))}</span>
                </div>
            @endif

            <form method="post" action="/register">
                <input
                        type="hidden"
                        name="${csrfParameterName}"
                        value="${csrfToken}"
                >

                <div class="form-grid">

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
                                placeholder="Choose a username"
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
                                placeholder="you@example.com"
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
                                placeholder="Given name"
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
                                placeholder="Family name"
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
                                    placeholder="Create a strong password"
                                    minlength="12"
                                    required
                                    aria-invalid="${errors.containsKey("password")}"
                            >

                            <button
                                    class="toggle-password"
                                    type="button"
                                    data-password-toggle="password"
                                    aria-label="Show password"
                                    aria-pressed="false"
                            >
                                <svg width="18" height="18" viewBox="0 0 24 24"
                                     fill="none" stroke="currentColor"
                                     stroke-width="1.8" stroke-linecap="round"
                                     stroke-linejoin="round" aria-hidden="true">
                                    <path d="M2 12s3.6-7 10-7 10 7 10 7-3.6 7-10 7-10-7-10-7Z"/>
                                    <circle cx="12" cy="12" r="3"/>
                                </svg>
                            </button>
                        </div>

                        <p class="password-hint">
                            Use at least 12 characters for your password.
                        </p>

                        @if (errors.containsKey("password"))
                            <p class="field-error" role="alert">
                                ${messages.get(errors.get("password"))}
                            </p>
                        @endif
                    </div>

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
                                    placeholder="Enter your password again"
                                    required
                                    aria-invalid="${errors.containsKey("confirmPassword")}"
                            >

                            <button
                                    class="toggle-password"
                                    type="button"
                                    data-password-toggle="confirmPassword"
                                    aria-label="Show password"
                                    aria-pressed="false"
                            >
                                <svg width="18" height="18" viewBox="0 0 24 24"
                                     fill="none" stroke="currentColor"
                                     stroke-width="1.8" stroke-linecap="round"
                                     stroke-linejoin="round" aria-hidden="true">
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

                <button class="submit-button" type="submit">
                    <span>${messages.get("register.title")}</span>
                    <span aria-hidden="true">→</span>
                </button>
            </form>

            <p class="login-link">
                Already have an account?
                <a href="/login">${messages.get("common.backToLogin")}</a>
            </p>

            <div class="security-note">
                <span aria-hidden="true">●</span>
                Your information is protected by secure authentication.
            </div>

            <div class="language-area">
                @template.partials.languageSwitcher(messages = messages)
            </div>

        </div>
    </main>

</div>

<script>
    document.querySelectorAll("[data-password-toggle]").forEach((button) => {
        button.addEventListener("click", () => {
            const inputId = button.dataset.passwordToggle;
            const input = document.getElementById(inputId);

            if (!input) {
                return;
            }

            const isVisible = input.type === "text";

            input.type = isVisible ? "password" : "text";
            button.setAttribute("aria-pressed", String(!isVisible));
            button.setAttribute(
                "aria-label",
                isVisible ? "Show password" : "Hide password"
            );
        });
    });
</script>
</body>
</html>