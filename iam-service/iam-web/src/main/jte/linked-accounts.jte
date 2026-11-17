@import kh.edu.istad.iam.domain.result.IdentityResult
@param kh.edu.istad.iam.web.i18n.Messages messages
@param String language
@param java.util.List<IdentityResult> identities
@param String errorKey
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

    <title>${messages.get("profile.linkedAccounts")} | IAM Platform</title>

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
            --danger: #b42318;
            --danger-light: #fef3f2;
            --success: #027a48;
            --success-light: #ecfdf3;
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
            grid-template-columns: 0.85fr 1.15fr;
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
            padding: 55px;
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
            font-size: 23px;
            font-weight: 800;
            letter-spacing: -0.5px;
        }

        .banner-content {
            position: relative;
            z-index: 1;
            max-width: 450px;
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
            margin-bottom: 24px;
            font-size: clamp(38px, 4vw, 55px);
            line-height: 1.15;
            letter-spacing: -2px;
        }

        .banner-content p {
            max-width: 410px;
            color: rgba(255, 255, 255, 0.8);
            font-size: 15px;
            line-height: 1.9;
        }

        .banner-footer {
            position: relative;
            z-index: 1;
            color: rgba(255, 255, 255, 0.65);
            font-size: 12px;
        }

        /* ========================================
           RIGHT SIDE - CONTENT
        ======================================== */

        .auth-panel {
            display: flex;
            justify-content: center;
            align-items: flex-start;
            padding: 55px 35px;
            background: #ffffff;
        }

        .content-card {
            width: 100%;
            max-width: 570px;
        }

        .page-header {
            margin-bottom: 28px;
        }

        .page-header h1 {
            margin-bottom: 10px;
            font-size: 34px;
            font-weight: 800;
            letter-spacing: -1.2px;
        }

        .page-header p {
            color: var(--muted);
            font-size: 14px;
            line-height: 1.8;
        }

        /* ========================================
           ERROR MESSAGE
        ======================================== */

        .error-message {
            display: flex;
            align-items: flex-start;
            gap: 11px;
            padding: 14px 16px;
            margin-bottom: 23px;
            border: 1px solid #fecdca;
            border-radius: 10px;
            background: var(--danger-light);
            color: var(--danger);
            font-size: 13px;
            line-height: 1.7;
        }

        .error-message svg {
            width: 19px;
            height: 19px;
            flex-shrink: 0;
            margin-top: 1px;
        }

        /* ========================================
           SECTION HEADER
        ======================================== */

        .section-header {
            display: flex;
            align-items: center;
            gap: 12px;
            margin-bottom: 18px;
        }

        .section-icon {
            display: grid;
            place-items: center;
            width: 42px;
            height: 42px;
            flex-shrink: 0;
            border-radius: 11px;
            background: var(--primary-light);
            color: var(--primary);
        }

        .section-icon svg {
            width: 21px;
            height: 21px;
        }

        .section-header h2 {
            font-size: 16px;
            font-weight: 800;
        }

        .section-header p {
            margin-top: 3px;
            color: var(--muted);
            font-size: 11px;
        }

        /* ========================================
           LINKED IDENTITIES
        ======================================== */

        .identity-section {
            padding: 21px;
            margin-bottom: 25px;
            border: 1px solid var(--border);
            border-radius: 13px;
            background: #ffffff;
        }

        .identity-list {
            display: flex;
            flex-direction: column;
            gap: 12px;
            list-style: none;
        }

        .identity-item {
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 15px;
            padding: 15px;
            border: 1px solid #eeeaf5;
            border-radius: 10px;
            background: #fcfbff;
        }

        .identity-info {
            display: flex;
            align-items: center;
            gap: 12px;
            min-width: 0;
        }

        .provider-icon {
            display: grid;
            place-items: center;
            width: 42px;
            height: 42px;
            flex-shrink: 0;
            border: 1px solid var(--border);
            border-radius: 10px;
            background: #ffffff;
            color: var(--primary-dark);
        }

        .provider-icon svg {
            width: 21px;
            height: 21px;
        }

        .identity-details {
            min-width: 0;
        }

        .provider-name {
            display: block;
            margin-bottom: 4px;
            color: var(--text);
            font-size: 13px;
            font-weight: 800;
            overflow-wrap: anywhere;
        }

        .identity-email {
            display: block;
            color: var(--muted);
            font-size: 12px;
            overflow-wrap: anywhere;
        }

        .unlink-form {
            flex-shrink: 0;
        }

        .unlink-btn {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            gap: 7px;
            min-height: 37px;
            padding: 8px 12px;
            border: 1px solid #fecdca;
            border-radius: 8px;
            background: var(--danger-light);
            color: var(--danger);
            font-size: 11px;
            font-weight: 700;
            cursor: pointer;
            transition: all 0.2s ease;
        }

        .unlink-btn:hover {
            border-color: #fda29b;
            background: #fee4e2;
        }

        .unlink-btn svg {
            width: 15px;
            height: 15px;
        }

        /* ========================================
           EMPTY STATE
        ======================================== */

        .empty-state {
            padding: 28px 15px;
            border: 1px dashed #d9d2e7;
            border-radius: 10px;
            background: #fcfbff;
            text-align: center;
        }

        .empty-icon {
            display: grid;
            place-items: center;
            width: 48px;
            height: 48px;
            margin: 0 auto 13px;
            border-radius: 50%;
            background: var(--primary-light);
            color: var(--primary);
        }

        .empty-icon svg {
            width: 23px;
            height: 23px;
        }

        .empty-state h3 {
            margin-bottom: 6px;
            font-size: 14px;
            font-weight: 800;
        }

        .empty-state p {
            color: var(--muted);
            font-size: 12px;
            line-height: 1.7;
        }

        /* ========================================
           LINK PROVIDERS
        ======================================== */

        .provider-section {
            margin-bottom: 25px;
        }

        .provider-section > p {
            margin-bottom: 15px;
            color: var(--muted);
            font-size: 13px;
            line-height: 1.8;
        }

        .provider-list {
            display: flex;
            flex-direction: column;
            gap: 11px;
        }

        .provider-link {
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 12px;
            min-height: 58px;
            padding: 12px 16px;
            border: 1px solid var(--border);
            border-radius: 10px;
            background: #ffffff;
            color: var(--text);
            text-decoration: none;
            transition: all 0.2s ease;
        }

        .provider-link:hover {
            border-color: #c4b5fd;
            background: #faf8ff;
            transform: translateY(-1px);
        }

        .provider-link-content {
            display: flex;
            align-items: center;
            gap: 12px;
            min-width: 0;
        }

        .provider-link .provider-icon {
            width: 36px;
            height: 36px;
            border: none;
            background: var(--primary-light);
        }

        .provider-link-name {
            font-size: 13px;
            font-weight: 700;
        }

        .provider-link-action {
            display: flex;
            align-items: center;
            gap: 6px;
            flex-shrink: 0;
            color: var(--primary);
            font-size: 11px;
            font-weight: 700;
        }

        .provider-link-action svg {
            width: 15px;
            height: 15px;
        }

        /* ========================================
           TELEGRAM WIDGET
        ======================================== */

        .telegram-section {
            padding: 20px;
            margin-bottom: 23px;
            border: 1px solid var(--border);
            border-radius: 12px;
            background: #fcfbff;
        }

        /* ========================================
           FOOTER NAVIGATION
        ======================================== */

        .footer-actions {
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 15px;
            padding-top: 20px;
            border-top: 1px solid var(--border);
        }

        .back-link {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            color: var(--primary);
            font-size: 13px;
            font-weight: 700;
            text-decoration: none;
        }

        .back-link:hover {
            color: var(--primary-dark);
            text-decoration: underline;
        }

        .back-link svg {
            width: 17px;
            height: 17px;
        }

        .language-section {
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

        @media (max-width: 1050px) {
            .auth-banner {
                padding: 40px 35px;
            }

            .auth-panel {
                padding: 40px 24px;
            }

            .banner-content h2 {
                font-size: 40px;
            }
        }

        @media (max-width: 800px) {
            .auth-container {
                grid-template-columns: 1fr;
            }

            .auth-banner {
                min-height: 280px;
                padding: 30px 25px;
            }

            .banner-content {
                padding: 40px 0 25px;
            }

            .banner-content h2 {
                font-size: 36px;
            }

            .auth-panel {
                padding: 40px 24px;
            }

            .content-card {
                max-width: 600px;
            }
        }

        @media (max-width: 520px) {
            .page-header h1 {
                font-size: 29px;
            }

            .identity-section {
                padding: 15px;
            }

            .identity-item {
                align-items: flex-start;
                flex-wrap: wrap;
            }

            .unlink-form {
                width: 100%;
            }

            .unlink-btn {
                width: 100%;
            }

            .footer-actions {
                align-items: flex-start;
                flex-direction: column;
            }
        }

        @media (max-width: 380px) {
            .auth-panel {
                padding: 30px 16px;
            }

            .page-header h1 {
                font-size: 26px;
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
    </style>
</head>

<body>

<div class="auth-container">

    <!-- ======================================
         LEFT SIDE: BRANDING
    ======================================= -->

    <section class="auth-banner">

        <div class="brand">
            IAM Platform
        </div>

        <div class="banner-content">

            <span class="banner-label">
                Connected Identity
            </span>

            <h2>One Identity. Multiple Connections.</h2>

            <p>
                Connect your trusted social accounts to make signing in
                easier. Manage your linked identities and maintain control
                over your account connections.
            </p>

        </div>

        <div class="banner-footer">
            © 2026 IAM Platform. All rights reserved.
        </div>

    </section>

    <!-- ======================================
         RIGHT SIDE: LINKED ACCOUNTS
    ======================================= -->

    <section class="auth-panel">

        <main class="content-card">

            <header class="page-header">

                <h1>
                    ${messages.get("profile.linkedAccounts")}
                </h1>

                <p>
                    Manage the external accounts connected to your
                    IAM identity.
                </p>

            </header>

            <!-- ERROR MESSAGE -->

            @if (!errorKey.isEmpty())

                <div class="error-message" role="alert">

                    <svg
                            viewBox="0 0 24 24"
                            fill="none"
                            stroke="currentColor"
                            stroke-width="2"
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            aria-hidden="true">

                        <circle cx="12" cy="12" r="10"/>
                        <path d="M12 8v4"/>
                        <path d="M12 16h.01"/>

                    </svg>

                    <span>
                        ${messages.get(errorKey)}
                    </span>

                </div>

            @endif

            <!-- ======================================
                 CURRENT LINKED ACCOUNTS
            ======================================= -->

            <section class="identity-section">

                <header class="section-header">

                    <div class="section-icon">

                        <svg
                                viewBox="0 0 24 24"
                                fill="none"
                                stroke="currentColor"
                                stroke-width="1.8"
                                stroke-linecap="round"
                                stroke-linejoin="round"
                                aria-hidden="true">

                            <circle cx="9" cy="8" r="4"/>
                            <path d="M3 21a6 6 0 0 1 12 0"/>
                            <path d="M16 11h5"/>
                            <path d="M18.5 8.5v5"/>

                        </svg>

                    </div>

                    <div>
                        <h2>Connected Accounts</h2>
                        <p>Your currently linked identities</p>
                    </div>

                </header>

                @if (!identities.isEmpty())

                    <ul class="identity-list">

                        @for (var identity : identities)

                            <li class="identity-item">

                                <div class="identity-info">

                                    <div class="provider-icon">

                                        <svg
                                                viewBox="0 0 24 24"
                                                fill="none"
                                                stroke="currentColor"
                                                stroke-width="1.8"
                                                stroke-linecap="round"
                                                stroke-linejoin="round"
                                                aria-hidden="true">

                                            <circle cx="12" cy="12" r="9"/>
                                            <path d="M8 12h8"/>
                                            <path d="M12 8v8"/>

                                        </svg>

                                    </div>

                                    <div class="identity-details">

                                        <span class="provider-name">
                                            ${identity.provider().name()}
                                        </span>

                                        <span class="identity-email">
                                            ${identity.email()}
                                        </span>

                                    </div>

                                </div>

                                <form
                                        class="unlink-form"
                                        method="post"
                                        action="/profile/accounts/${identity.id().toString()}/unlink">

                                    <input
                                            type="hidden"
                                            name="${csrfParameterName}"
                                            value="${csrfToken}">

                                    <button
                                            class="unlink-btn"
                                            type="submit">

                                        <svg
                                                viewBox="0 0 24 24"
                                                fill="none"
                                                stroke="currentColor"
                                                stroke-width="1.8"
                                                stroke-linecap="round"
                                                stroke-linejoin="round"
                                                aria-hidden="true">

                                            <path d="M3 6h18"/>
                                            <path d="M8 6V4h8v2"/>
                                            <path d="m19 6-1 14H6L5 6"/>
                                            <path d="M10 11v5"/>
                                            <path d="M14 11v5"/>

                                        </svg>

                                        ${messages.get("common.unlink")}

                                    </button>

                                </form>

                            </li>

                        @endfor

                    </ul>

                @else

                    <div class="empty-state">

                        <div class="empty-icon">

                            <svg
                                    viewBox="0 0 24 24"
                                    fill="none"
                                    stroke="currentColor"
                                    stroke-width="1.8"
                                    stroke-linecap="round"
                                    stroke-linejoin="round"
                                    aria-hidden="true">

                                <circle cx="12" cy="12" r="9"/>
                                <path d="M12 8v8"/>
                                <path d="M8 12h8"/>

                            </svg>

                        </div>

                        <h3>No Linked Accounts</h3>

                        <p>
                            You haven't connected any external identity
                            providers yet. Connect an account below.
                        </p>

                    </div>

                @endif

            </section>

            <!-- ======================================
                 AVAILABLE IDENTITY PROVIDERS
            ======================================= -->

            <section class="provider-section">

                <header class="section-header">

                    <div class="section-icon">

                        <svg
                                viewBox="0 0 24 24"
                                fill="none"
                                stroke="currentColor"
                                stroke-width="1.8"
                                stroke-linecap="round"
                                stroke-linejoin="round"
                                aria-hidden="true">

                            <path d="M12 5v14"/>
                            <path d="M5 12h14"/>

                        </svg>

                    </div>

                    <div>
                        <h2>Connect Another Account</h2>
                        <p>Choose an identity provider</p>
                    </div>

                </header>

                <p>
                    ${messages.get("profile.linkInstructions")}
                </p>

                <div class="provider-list">

                    @if (googleEnabled)

                        <a
                                class="provider-link"
                                href="/oauth2/authorization/google">

                            <span class="provider-link-content">

                                <span class="provider-icon">

                                    <svg
                                            viewBox="0 0 24 24"
                                            aria-hidden="true">

                                        <path
                                                fill="#4285F4"
                                                d="M21.6 12.23c0-.71-.06-1.39-.18-2.05H12v3.87h5.38a4.6 4.6 0 0 1-2 3.02v2.51h3.23c1.89-1.74 2.99-4.3 2.99-7.35Z"/>

                                        <path
                                                fill="#34A853"
                                                d="M12 22c2.7 0 4.96-.9 6.61-2.42l-3.23-2.51c-.9.6-2.05.96-3.38.96-2.6 0-4.81-1.75-5.6-4.1H3.06v2.59A10 10 0 0 0 12 22Z"/>

                                        <path
                                                fill="#FBBC05"
                                                d="M6.4 13.93a6.02 6.02 0 0 1 0-3.86V7.48H3.06a10 10 0 0 0 0 9.04l3.34-2.59Z"/>

                                        <path
                                                fill="#EA4335"
                                                d="M12 5.97c1.47 0 2.79.5 3.83 1.5l2.87-2.87C16.95 2.97 14.7 2 12 2a10 10 0 0 0-8.94 5.48L6.4 10.07c.79-2.35 3-4.1 5.6-4.1Z"/>

                                    </svg>

                                </span>

                                <span class="provider-link-name">
                                    ${messages.get("login.continueWith", "Google")}
                                </span>

                            </span>

                            <span class="provider-link-action">

                                Connect

                                <svg
                                        viewBox="0 0 24 24"
                                        fill="none"
                                        stroke="currentColor"
                                        stroke-width="2"
                                        stroke-linecap="round"
                                        stroke-linejoin="round"
                                        aria-hidden="true">

                                    <path d="M7 17 17 7"/>
                                    <path d="M7 7h10v10"/>

                                </svg>

                            </span>

                        </a>

                    @endif

                    @if (githubEnabled)

                        <a
                                class="provider-link"
                                href="/oauth2/authorization/github">

                            <span class="provider-link-content">

                                <span class="provider-icon">

                                    <svg
                                            viewBox="0 0 24 24"
                                            fill="currentColor"
                                            aria-hidden="true">

                                        <path d="M12 .9a11.1 11.1 0 0 0-3.51 21.63c.55.1.76-.24.76-.53v-2.06c-3.1.67-3.76-1.31-3.76-1.31-.5-1.28-1.23-1.62-1.23-1.62-1.01-.69.08-.68.08-.68 1.12.08 1.71 1.15 1.71 1.15.99 1.7 2.6 1.21 3.23.92.1-.72.39-1.21.71-1.49-2.47-.28-5.07-1.24-5.07-5.52 0-1.22.44-2.22 1.15-3-.12-.28-.5-1.42.11-2.95 0 0 .94-.3 3.06 1.15a10.65 10.65 0 0 1 5.57 0c2.12-1.45 3.06-1.15 3.06-1.15.61 1.53.23 2.67.11 2.95.72.78 1.15 1.78 1.15 3.01 0 4.29-2.6 5.23-5.08 5.5.4.35.76 1.03.76 2.08V22c0 .29.2.64.77.53A11.1 11.1 0 0 0 12 .9Z"/>

                                    </svg>

                                </span>

                                <span class="provider-link-name">
                                    ${messages.get("login.continueWith", "GitHub")}
                                </span>

                            </span>

                            <span class="provider-link-action">

                                Connect

                                <svg
                                        viewBox="0 0 24 24"
                                        fill="none"
                                        stroke="currentColor"
                                        stroke-width="2"
                                        stroke-linecap="round"
                                        stroke-linejoin="round"
                                        aria-hidden="true">

                                    <path d="M7 17 17 7"/>
                                    <path d="M7 7h10v10"/>

                                </svg>

                            </span>

                        </a>

                    @endif

                    @if (facebookEnabled)

                        <a
                                class="provider-link"
                                href="/oauth2/authorization/facebook">

                            <span class="provider-link-content">

                                <span class="provider-icon">

                                    <svg
                                            viewBox="0 0 24 24"
                                            fill="#1877F2"
                                            aria-hidden="true">

                                        <path d="M24 12a12 12 0 1 0-13.88 11.85v-8.39H7.08V12h3.04V9.36c0-3 1.79-4.66 4.53-4.66 1.31 0 2.69.24 2.69.24v2.96h-1.51c-1.49 0-1.95.92-1.95 1.87V12h3.32l-.53 3.46h-2.79v8.39A12 12 0 0 0 24 12Z"/>

                                    </svg>

                                </span>

                                <span class="provider-link-name">
                                    ${messages.get("login.continueWith", "Facebook")}
                                </span>

                            </span>

                            <span class="provider-link-action">

                                Connect

                                <svg
                                        viewBox="0 0 24 24"
                                        fill="none"
                                        stroke="currentColor"
                                        stroke-width="2"
                                        stroke-linecap="round"
                                        stroke-linejoin="round"
                                        aria-hidden="true">

                                    <path d="M7 17 17 7"/>
                                    <path d="M7 7h10v10"/>

                                </svg>

                            </span>

                        </a>

                    @endif

                </div>

            </section>

            <!-- ======================================
                 TELEGRAM WIDGET
            ======================================= -->

            <section class="telegram-section">

                @template.partials.telegramWidget(
                messages = messages,
                botUsername = telegramBotUsername
                )

            </section>

            <!-- ======================================
                 FOOTER ACTIONS
            ======================================= -->

            <footer class="footer-actions">

                <a class="back-link" href="/">

                    <svg
                            viewBox="0 0 24 24"
                            fill="none"
                            stroke="currentColor"
                            stroke-width="2"
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            aria-hidden="true">

                        <path d="m15 18-6-6 6-6"/>
                        <path d="M9 12h12"/>

                    </svg>

                    ${messages.get("common.back")}

                </a>

                <div class="language-section">

                    @template.partials.languageSwitcher(messages = messages)

                </div>

            </footer>

        </main>

    </section>

</div>

</body>
</html>