@import kh.edu.istad.iam.domain.result.ConnectedApplicationResult
@param kh.edu.istad.iam.web.i18n.Messages messages
@param String language
@param java.util.List<ConnectedApplicationResult> apps
@param String csrfToken
@param String csrfParameterName

<!doctype html>
<html lang="${language}">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="theme-color" content="#6d28d9">

    <title>${messages.get("profile.connectedApps")} | IAM Platform</title>

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
           RIGHT SIDE - APPLICATIONS PANEL
        ======================================== */

        .auth-panel {
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 45px 30px;
            background: #ffffff;
        }

        .apps-card {
            width: 100%;
            max-width: 490px;
        }

        .apps-header {
            margin-bottom: 30px;
        }

        .apps-header h1 {
            font-size: 34px;
            font-weight: 800;
            letter-spacing: -1.2px;
            margin-bottom: 11px;
        }

        .apps-header p {
            color: var(--muted);
            font-size: 14px;
            line-height: 1.8;
        }

        /* ========================================
           APPLICATION COUNT
        ======================================== */

        .apps-summary {
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 12px;
            margin-bottom: 18px;
        }

        .apps-summary span {
            color: var(--muted);
            font-size: 12px;
            font-weight: 600;
        }

        .apps-count {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            min-width: 29px;
            height: 29px;
            padding: 0 9px;
            border-radius: 20px;
            background: var(--primary-light);
            color: var(--primary-dark) !important;
            font-size: 12px !important;
            font-weight: 800 !important;
        }

        /* ========================================
           APPLICATION LIST
        ======================================== */

        .apps-list {
            display: flex;
            flex-direction: column;
            gap: 13px;
            list-style: none;
        }

        .app-item {
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 16px;
            padding: 19px;
            border: 1px solid var(--border);
            border-radius: 13px;
            background: #ffffff;
            transition:
                    border-color 0.2s ease,
                    box-shadow 0.2s ease,
                    transform 0.2s ease;
        }

        .app-item:hover {
            border-color: #c4b5fd;
            box-shadow: 0 5px 18px rgba(124, 58, 237, 0.07);
            transform: translateY(-1px);
        }

        .app-info {
            min-width: 0;
            flex: 1;
        }

        .app-name {
            display: block;
            margin-bottom: 7px;
            color: #343047;
            font-size: 14px;
            font-weight: 700;
            overflow-wrap: anywhere;
        }

        .app-scopes {
            color: var(--muted);
            font-size: 12px;
            line-height: 1.7;
            overflow-wrap: anywhere;
        }

        /* ========================================
           REVOKE BUTTON
        ======================================== */

        .revoke-form {
            flex-shrink: 0;
        }

        .revoke-btn {
            min-height: 37px;
            padding: 0 13px;
            border: 1px solid #fecdca;
            border-radius: 8px;
            background: var(--danger-light);
            color: var(--danger);
            font-size: 12px;
            font-weight: 700;
            cursor: pointer;
            transition:
                    background 0.2s ease,
                    border-color 0.2s ease,
                    transform 0.2s ease;
        }

        .revoke-btn:hover {
            background: #fee4e2;
            border-color: #fda29b;
            transform: translateY(-1px);
        }

        .revoke-btn:active {
            transform: translateY(0);
        }

        /* ========================================
           EMPTY STATE
        ======================================== */

        .empty-state {
            display: flex;
            flex-direction: column;
            align-items: center;
            padding: 40px 25px;
            border: 1px dashed #d8d2e5;
            border-radius: 14px;
            background: #fcfbff;
            text-align: center;
        }

        .empty-icon {
            display: grid;
            place-items: center;
            width: 58px;
            height: 58px;
            margin-bottom: 18px;
            border-radius: 17px;
            background: var(--primary-light);
            color: var(--primary);
        }

        .empty-icon svg {
            width: 28px;
            height: 28px;
        }

        .empty-state h2 {
            margin-bottom: 9px;
            font-size: 16px;
            font-weight: 700;
        }

        .empty-state p {
            max-width: 300px;
            color: var(--muted);
            font-size: 13px;
            line-height: 1.8;
        }

        /* ========================================
           SECURITY INFORMATION
        ======================================== */

        .security-note {
            display: flex;
            align-items: flex-start;
            gap: 11px;
            margin-top: 22px;
            padding: 14px;
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
                padding: 40px 22px;
            }

            .app-item {
                align-items: flex-start;
                flex-direction: column;
            }

            .revoke-form,
            .revoke-btn {
                width: 100%;
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

            .apps-header h1 {
                font-size: 29px;
            }

            .app-item {
                align-items: center;
                flex-direction: row;
            }

            .revoke-form {
                width: auto;
            }

            .revoke-btn {
                width: auto;
            }
        }

        @media (max-width: 420px) {
            .app-item {
                align-items: stretch;
                flex-direction: column;
            }

            .revoke-form,
            .revoke-btn {
                width: 100%;
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

            .apps-header h1 {
                font-size: 26px;
            }
        }

        /* Respect reduced-motion preferences. */
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
            <span>IAM Platform</span>
        </div>

        <div class="banner-content">

            <span class="banner-label">
                Connected Services
            </span>

            <h2>Your Apps. Your Identity. Your Control.</h2>

            <p>
                Manage applications connected to your account.
                Review their granted permissions and revoke access
                whenever you no longer trust or need an application.
            </p>

        </div>

        <div class="banner-footer">
            © 2026 IAM Platform. All rights reserved.
        </div>

    </section>

    <!-- ======================================
         RIGHT SIDE: CONNECTED APPLICATIONS
    ======================================= -->

    <section class="auth-panel">

        <main class="apps-card">

            <header class="apps-header">

                <h1>
                    ${messages.get("profile.connectedApps")}
                </h1>

                <p>
                    Review the applications connected to your account
                    and manage their access permissions.
                </p>

            </header>

            @if (!apps.isEmpty())

                <div class="apps-summary">
                    <span>Applications with account access</span>

                    <span class="apps-count">
                        ${apps.size()}
                    </span>
                </div>

                <ul class="apps-list">

                    @for (var app : apps)

                        <li class="app-item">

                            <div class="app-info">

                                <strong class="app-name">
                                    ${app.name()}
                                </strong>

                                <p class="app-scopes">
                                    ${app.scopes()}
                                </p>

                            </div>

                            <form
                                    class="revoke-form"
                                    method="post"
                                    action="/profile/apps/${app.id()}/revoke">

                                <input
                                        type="hidden"
                                        name="${csrfParameterName}"
                                        value="${csrfToken}">

                                <button
                                        class="revoke-btn"
                                        type="submit">
                                    ${messages.get("common.revoke")}
                                </button>

                            </form>

                        </li>

                    @endfor

                </ul>

            @else

                <section class="empty-state">

                    <div class="empty-icon">

                        <svg
                                viewBox="0 0 24 24"
                                fill="none"
                                stroke="currentColor"
                                stroke-width="1.7"
                                stroke-linecap="round"
                                stroke-linejoin="round"
                                aria-hidden="true">

                            <rect x="3" y="4" width="18" height="16" rx="2"/>
                            <path d="M8 9h8"/>
                            <path d="M8 13h5"/>
                            <path d="M8 17h3"/>

                        </svg>

                    </div>

                    <h2>No Connected Applications</h2>

                    <p>
                        You haven't connected any external applications
                        to your account yet. Applications you authorize
                        will appear here.
                    </p>

                </section>

            @endif

            <!-- SECURITY INFORMATION -->

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
                    Only authorize applications you trust.
                    Revoking access prevents an application from
                    continuing to use its existing authorization.
                </p>

            </div>

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

</body>
</html>