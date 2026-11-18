@param kh.edu.istad.iam.web.i18n.Messages messages
@param String language
@param String clientId
@param String clientName
@param String state
@param java.util.List<String> scopes
@param String csrfToken
@param String csrfParameterName

<!doctype html>
<html lang="${language}">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="theme-color" content="#6d28d9">

<title>${messages.get("consent.title")} | ${messages.get("brand.name")}</title>

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
            --success: #027a48;
            --success-light: #ecfdf3;
            --danger: #b42318;
            --danger-light: #fef3f2;
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

        button {
            font: inherit;
        }

        a {
            color: inherit;
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
           RIGHT SIDE - CONSENT PANEL
        ======================================== */

        .auth-panel {
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 45px 30px;
            background: #ffffff;
        }

        .consent-card {
            width: 100%;
            max-width: 470px;
        }

        .consent-header {
            margin-bottom: 28px;
        }

        .consent-header h1 {
            font-size: 34px;
            font-weight: 800;
            letter-spacing: -1.2px;
            margin-bottom: 11px;
        }

        .consent-header p {
            color: var(--muted);
            font-size: 14px;
            line-height: 1.8;
        }

        /* ========================================
           CLIENT INFORMATION
        ======================================== */

        .client-card {
            display: flex;
            align-items: center;
            gap: 15px;
            padding: 17px;
            margin-bottom: 25px;
            border: 1px solid var(--border);
            border-radius: 13px;
            background: #fcfbff;
        }

        .client-icon {
            display: grid;
            place-items: center;
            width: 48px;
            height: 48px;
            flex-shrink: 0;
            border-radius: 13px;
            background: var(--primary-light);
            color: var(--primary);
        }

        .client-icon svg {
            width: 25px;
            height: 25px;
        }

        .client-info {
            min-width: 0;
        }

        .client-label {
            display: block;
            margin-bottom: 4px;
            color: var(--muted);
            font-size: 11px;
            font-weight: 600;
        }

        .client-name {
            color: #343047;
            font-size: 15px;
            font-weight: 800;
            overflow-wrap: anywhere;
        }

        /* ========================================
           PERMISSIONS
        ======================================== */

        .permissions-header {
            margin-bottom: 13px;
        }

        .permissions-header h2 {
            font-size: 15px;
            font-weight: 800;
            margin-bottom: 5px;
        }

        .permissions-header p {
            color: var(--muted);
            font-size: 12px;
            line-height: 1.7;
        }

        .permissions-list {
            display: flex;
            flex-direction: column;
            gap: 11px;
            margin-bottom: 23px;
            list-style: none;
        }

        .permission-item {
            display: flex;
            align-items: flex-start;
            gap: 12px;
            padding: 14px;
            border: 1px solid var(--border);
            border-radius: 11px;
            background: #ffffff;
        }

        .permission-icon {
            display: grid;
            place-items: center;
            width: 34px;
            height: 34px;
            flex-shrink: 0;
            border-radius: 9px;
            background: var(--success-light);
            color: var(--success);
        }

        .permission-icon svg {
            width: 18px;
            height: 18px;
        }

        .permission-info {
            min-width: 0;
            flex: 1;
        }

        .permission-name {
            display: block;
            margin-bottom: 4px;
            color: #343047;
            font-size: 13px;
            font-weight: 700;
            overflow-wrap: anywhere;
        }

        .permission-description {
            color: var(--muted);
            font-size: 12px;
            line-height: 1.7;
            overflow-wrap: anywhere;
        }

        /* ========================================
           SECURITY NOTICE
        ======================================== */

        .security-note {
            display: flex;
            align-items: flex-start;
            gap: 11px;
            padding: 14px;
            margin-bottom: 23px;
            border: 1px solid #ddd6fe;
            border-radius: 10px;
            background: #faf8ff;
        }

        .security-note svg {
            width: 19px;
            height: 19px;
            flex-shrink: 0;
            color: var(--primary);
        }

        .security-note p {
            color: #655b7b;
            font-size: 11px;
            line-height: 1.7;
        }

        /* ========================================
           ACTION BUTTONS
        ======================================== */

        .consent-actions {
            display: flex;
            flex-direction: column;
            gap: 11px;
        }

        .allow-btn,
        .cancel-btn {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 10px;
            width: 100%;
            min-height: 51px;
            border-radius: 11px;
            font-size: 14px;
            font-weight: 700;
            cursor: pointer;
            transition: all 0.2s ease;
        }

        .allow-btn {
            border: none;
            background: linear-gradient(
                    135deg,
                    #7c3aed,
                    #9333ea
            );
            color: white;
            box-shadow: 0 7px 18px rgba(124, 58, 237, 0.2);
        }

        .allow-btn:hover {
            background: linear-gradient(
                    135deg,
                    #6d28d9,
                    #7e22ce
            );
            transform: translateY(-2px);
            box-shadow: 0 10px 24px rgba(124, 58, 237, 0.3);
        }

        .cancel-btn {
            border: 1px solid var(--border);
            background: #ffffff;
            color: #655b7b;
        }

        .cancel-btn:hover {
            border-color: #fda29b;
            background: var(--danger-light);
            color: var(--danger);
        }

        .allow-btn:active,
        .cancel-btn:active {
            transform: translateY(0);
        }

        .allow-btn svg,
        .cancel-btn svg {
            width: 18px;
            height: 18px;
        }

        /* ========================================
           LANGUAGE SWITCHER
        ======================================== */

        .language-section {
            margin-top: 25px;
            text-align: center;
            font-size: 13px;
        }

        .language-section a {
            color: var(--primary);
            font-weight: 700;
            text-decoration: none;
        }

        .language-section a:hover {
            text-decoration: underline;
        }

        /* ========================================
           ACCESSIBILITY
        ======================================== */

        a:focus-visible,
        button:focus-visible {
            outline: 3px solid rgba(124, 58, 237, 0.3);
            outline-offset: 3px;
        }

        /* ========================================
           RESPONSIVE
        ======================================== */

        @media (max-width: 1000px) {
            .auth-banner {
                padding: 40px 35px;
            }

            .banner-content h2 {
                font-size: 40px;
            }
        }

        @media (max-width: 800px) {
            .auth-container {
                grid-template-columns: 0.85fr 1.15fr;
            }

            .auth-panel {
                padding: 35px 22px;
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

            .consent-header h1 {
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

            .consent-header h1 {
                font-size: 26px;
            }

            .client-card {
                padding: 13px;
            }
        }

        @media (prefers-reduced-motion: reduce) {
            *,
            *::before,
            *::after {
                scroll-behavior: auto !important;
                transition-duration: 0.01ms !important;
                animation-duration: 0.01ms !important;
                animation-iteration-count: 1 !important;
            }
        }
        /* Single-column consent page */
        .auth-container { display: block; max-width: 920px; margin: 0 auto; padding: 32px 20px; }
        .auth-banner { min-height: auto; margin-bottom: 24px; padding: 28px 32px; border-radius: 20px; }
        .auth-panel { min-height: auto; padding: 0; background: transparent; box-shadow: none; }
        .consent-card { max-width: 760px; margin: 0 auto; }
        .allow-btn:hover, .cancel-btn:hover, .language-section a:hover { border-color: var(--primary); color: var(--primary-dark); background: var(--primary-light); }
        @media (max-width: 700px) { .auth-container { padding: 20px 14px; } .auth-banner { padding: 24px; } }
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

            <span class="banner-label">
                OAuth2 Authorization
            </span>

                <h2>${messages.get("consent.heroTitle")}</h2>

            <p>
                ${messages.get("consent.heroText")}
            </p>

        </div>

        <div class="banner-footer">
            © 2026 ${messages.get("brand.name")}. ${messages.get("common.allRightsReserved")}
        </div>

    </section>

    <!-- ======================================
         RIGHT SIDE: CONSENT FORM
    ======================================= -->

    <section class="auth-panel">

        <main class="consent-card">

            <header class="consent-header">

                <h1>
                    ${messages.get("consent.title")}
                </h1>

                <p>
                    ${messages.get("consent.description", clientName)}
                </p>

            </header>

            <!-- CLIENT INFORMATION -->

            <div class="client-card">

                <div class="client-icon">

                    <svg
                            viewBox="0 0 24 24"
                            fill="none"
                            stroke="currentColor"
                            stroke-width="1.8"
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            aria-hidden="true">

                        <rect x="3" y="4" width="18" height="16" rx="2"/>
                        <path d="M8 9h8"/>
                        <path d="M8 13h5"/>
                        <path d="M8 17h3"/>

                    </svg>

                </div>

                <div class="client-info">

                    <span class="client-label">
                        ${messages.get("consent.clientLabel")}
                    </span>

                    <strong class="client-name">
                        ${clientName}
                    </strong>

                </div>

            </div>

            <!-- REQUESTED PERMISSIONS -->

            <div class="permissions-header">

                <h2>${messages.get("consent.requestedScopes")}</h2>

                <p>
                    ${messages.get("consent.requestedDescription")}
                </p>

            </div>

            <ul class="permissions-list">

                @for (var scope : scopes)

                    <li class="permission-item">

                        <div class="permission-icon">

                            <svg
                                    viewBox="0 0 24 24"
                                    fill="none"
                                    stroke="currentColor"
                                    stroke-width="2"
                                    stroke-linecap="round"
                                    stroke-linejoin="round"
                                    aria-hidden="true">

                                <path d="M20 6 9 17l-5-5"/>

                            </svg>

                        </div>

                        <div class="permission-info">

                            <strong class="permission-name">
                                ${scope}
                            </strong>

                            <p class="permission-description">
                                ${messages.get("consent.scope." + scope)}
                            </p>

                        </div>

                    </li>

                @endfor

            </ul>

            <!-- SECURITY NOTICE -->

            <div class="security-note">

                <svg
                        viewBox="0 0 24 24"
                        fill="none"
                        stroke="currentColor"
                        stroke-width="1.8"
                        stroke-linecap="round"
                        stroke-linejoin="round"
                        aria-hidden="true">

                    <path d="M12 22s8-4 8-11V5l-8-3-8 3v6c0 7 8 11 8 11Z"/>
                    <path d="m9 12 2 2 4-4"/>

                </svg>

                <p>
                    ${messages.get("consent.securityNotice")}
                </p>

            </div>

            <!-- CONSENT ACTIONS -->

            <div class="consent-actions">

                <!-- ALLOW: Re-submit requested scopes -->

                <form method="post" action="/oauth2/authorize">

                    <input
                            type="hidden"
                            name="${csrfParameterName}"
                            value="${csrfToken}">

                    <input
                            type="hidden"
                            name="client_id"
                            value="${clientId}">

                    <input
                            type="hidden"
                            name="state"
                            value="${state}">

                    @for (var scope : scopes)

                        <input
                                type="hidden"
                                name="scope"
                                value="${scope}">

                    @endfor

                    <button
                            class="allow-btn"
                            type="submit">

                        <svg
                                viewBox="0 0 24 24"
                                fill="none"
                                stroke="currentColor"
                                stroke-width="2"
                                stroke-linecap="round"
                                stroke-linejoin="round"
                                aria-hidden="true">

                            <path d="m5 12 4 4L19 6"/>

                        </svg>

                        ${messages.get("consent.allow")}

                    </button>

                </form>

                <!-- CANCEL: Do not submit any scope parameter -->

                <form method="post" action="/oauth2/authorize">

                    <input
                            type="hidden"
                            name="${csrfParameterName}"
                            value="${csrfToken}">

                    <input
                            type="hidden"
                            name="client_id"
                            value="${clientId}">

                    <input
                            type="hidden"
                            name="state"
                            value="${state}">

                    <button
                            class="cancel-btn"
                            type="submit">

                        <svg
                                viewBox="0 0 24 24"
                                fill="none"
                                stroke="currentColor"
                                stroke-width="1.8"
                                stroke-linecap="round"
                                stroke-linejoin="round"
                                aria-hidden="true">

                            <path d="m18 6-12 12"/>
                            <path d="m6 6 12 12"/>

                        </svg>

                        ${messages.get("consent.cancel")}

                    </button>

                </form>

            </div>

            <!-- LANGUAGE SWITCHER -->

            <div class="language-section">

                @template.partials.languageSwitcher(messages = messages)

            </div>

        </main>

    </section>

</div>

</body>
</html>
