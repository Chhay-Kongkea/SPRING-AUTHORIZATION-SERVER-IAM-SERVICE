@param kh.edu.istad.iam.web.i18n.Messages messages
@param String language
@param String csrfToken
@param String csrfParameterName
@param java.util.Map<String, String> claims
@param java.util.List<String> roles
@param java.util.List<String> permissions
@param String notice

<!doctype html>
<html lang="${language}">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="theme-color" content="#6d28d9">

    <title>${messages.get("profile.title")} | IAM Platform</title>

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
            padding: 55px 55px;
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
            font-size: clamp(38px, 4vw, 55px);
            line-height: 1.15;
            letter-spacing: -2px;
            margin-bottom: 24px;
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
           RIGHT SIDE - PROFILE DASHBOARD
        ======================================== */

        .auth-panel {
            display: flex;
            justify-content: center;
            align-items: flex-start;
            padding: 45px 35px;
            background: #ffffff;
        }

        .profile-card {
            width: 100%;
            max-width: 650px;
        }

        .profile-header {
            margin-bottom: 25px;
        }

        .profile-header h1 {
            font-size: 34px;
            font-weight: 800;
            letter-spacing: -1.2px;
            margin-bottom: 10px;
        }

        .profile-header p {
            color: var(--muted);
            font-size: 14px;
            line-height: 1.8;
        }

        /* ========================================
           NOTICE MESSAGE
        ======================================== */

        .notice {
            display: flex;
            align-items: flex-start;
            gap: 11px;
            padding: 14px 16px;
            margin-bottom: 23px;
            border: 1px solid #abefc6;
            border-radius: 10px;
            background: var(--success-light);
            color: var(--success);
            font-size: 13px;
            line-height: 1.7;
        }

        .notice svg {
            width: 19px;
            height: 19px;
            flex-shrink: 0;
            margin-top: 1px;
        }

        /* ========================================
           SECTION CARD
        ======================================== */

        .section-card {
            padding: 21px;
            margin-bottom: 18px;
            border: 1px solid var(--border);
            border-radius: 13px;
            background: #ffffff;
        }

        .section-header {
            display: flex;
            align-items: center;
            gap: 11px;
            margin-bottom: 19px;
        }

        .section-icon {
            display: grid;
            place-items: center;
            width: 39px;
            height: 39px;
            flex-shrink: 0;
            border-radius: 11px;
            background: var(--primary-light);
            color: var(--primary);
        }

        .section-icon svg {
            width: 20px;
            height: 20px;
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
           CLAIMS
        ======================================== */

        .claims-list {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 12px;
        }

        .claim-item {
            min-width: 0;
            padding: 13px;
            border: 1px solid #eeeaf5;
            border-radius: 9px;
            background: #fcfbff;
        }

        .claim-key {
            display: block;
            margin-bottom: 6px;
            color: var(--muted);
            font-size: 11px;
            font-weight: 700;
            overflow-wrap: anywhere;
        }

        .claim-value {
            color: #343047;
            font-size: 13px;
            font-weight: 600;
            line-height: 1.7;
            overflow-wrap: anywhere;
        }

        /* ========================================
           ROLES AND PERMISSIONS
        ======================================== */

        .tag-list {
            display: flex;
            flex-wrap: wrap;
            gap: 9px;
            list-style: none;
        }

        .tag {
            display: inline-flex;
            align-items: center;
            gap: 7px;
            min-height: 34px;
            padding: 7px 11px;
            border: 1px solid #ddd6fe;
            border-radius: 8px;
            background: #faf8ff;
            color: var(--primary-dark);
            font-size: 12px;
            font-weight: 700;
            overflow-wrap: anywhere;
        }

        .tag svg {
            width: 14px;
            height: 14px;
            flex-shrink: 0;
        }

        .permission-tag {
            border-color: #d1e9ff;
            background: #eff8ff;
            color: #175cd3;
        }

        /* ========================================
           EMPTY STATE
        ======================================== */

        .empty-text {
            color: var(--muted);
            font-size: 13px;
            line-height: 1.7;
        }

        /* ========================================
           PROFILE NAVIGATION
        ======================================== */

        .navigation-section {
            margin-top: 25px;
        }

        .navigation-title {
            margin-bottom: 13px;
            font-size: 15px;
            font-weight: 800;
        }

        .navigation-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 11px;
        }

        .navigation-link {
            display: flex;
            align-items: center;
            gap: 12px;
            min-width: 0;
            padding: 14px;
            border: 1px solid var(--border);
            border-radius: 11px;
            background: #ffffff;
            color: #343047;
            text-decoration: none;
            transition:
                    border-color 0.2s ease,
                    background 0.2s ease,
                    transform 0.2s ease;
        }

        .navigation-link:hover {
            border-color: #c4b5fd;
            background: #faf8ff;
            transform: translateY(-2px);
        }

        .navigation-icon {
            display: grid;
            place-items: center;
            width: 36px;
            height: 36px;
            flex-shrink: 0;
            border-radius: 9px;
            background: var(--primary-light);
            color: var(--primary);
        }

        .navigation-icon svg {
            width: 19px;
            height: 19px;
        }

        .navigation-text {
            min-width: 0;
            font-size: 12px;
            font-weight: 700;
            line-height: 1.5;
            overflow-wrap: anywhere;
        }

        /* ========================================
           LOGOUT
        ======================================== */

        .logout-form {
            margin-top: 23px;
        }

        .logout-btn {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 10px;
            width: 100%;
            min-height: 48px;
            border: 1px solid #fecdca;
            border-radius: 10px;
            background: var(--danger-light);
            color: var(--danger);
            font-size: 13px;
            font-weight: 700;
            cursor: pointer;
            transition: all 0.2s ease;
        }

        .logout-btn:hover {
            border-color: #fda29b;
            background: #fee4e2;
            transform: translateY(-1px);
        }

        .logout-btn svg {
            width: 18px;
            height: 18px;
        }

        /* ========================================
           LANGUAGE SWITCHER
        ======================================== */

        .language-section {
            margin-top: 22px;
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

        @media (max-width: 1050px) {
            .auth-banner {
                padding: 40px 35px;
            }

            .auth-panel {
                padding: 35px 24px;
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

            .profile-card {
                max-width: 650px;
            }
        }

        @media (max-width: 500px) {
            .profile-header h1 {
                font-size: 29px;
            }

            .claims-list,
            .navigation-grid {
                grid-template-columns: 1fr;
            }

            .section-card {
                padding: 16px;
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
                padding: 30px 16px;
            }

            .profile-header h1 {
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
            <span>IAM Platform</span>
        </div>

        <div class="banner-content">

            <span class="banner-label">
                Identity Dashboard
            </span>

            <h2>Your Identity. Your Security. Your Control.</h2>

            <p>
                Manage your personal information, review your account
                permissions, and control the applications connected
                to your digital identity.
            </p>

        </div>

        <div class="banner-footer">
            © 2026 IAM Platform. All rights reserved.
        </div>

    </section>

    <!-- ======================================
         RIGHT SIDE: PROFILE DASHBOARD
    ======================================= -->

    <section class="auth-panel">

        <main class="profile-card">

            <header class="profile-header">

                <h1>
                    ${messages.get("profile.title")}
                </h1>

                <p>
                    Manage your account information, roles,
                    permissions, and security settings.
                </p>

            </header>

            <!-- NOTICE MESSAGE -->

            @if (!notice.isEmpty())

                <div class="notice" role="status">

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

                    <span>
                        ${messages.get(notice)}
                    </span>

                </div>

            @endif

            <!-- ======================================
                 ACCOUNT CLAIMS
            ======================================= -->

            <section class="section-card">

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

                            <circle cx="12" cy="8" r="4"/>
                            <path d="M5 21a7 7 0 0 1 14 0"/>

                        </svg>

                    </div>

                    <div>
                        <h2>${messages.get("profile.claims")}</h2>
                        <p>Your account identity information</p>
                    </div>

                </header>

                @if (!claims.isEmpty())

                    <dl class="claims-list">

                        @for (var claim : claims.entrySet())

                            <div class="claim-item">

                                <dt class="claim-key">
                                    ${claim.getKey()}
                                </dt>

                                <dd class="claim-value">
                                    ${claim.getValue()}
                                </dd>

                            </div>

                        @endfor

                    </dl>

                @else

                    <p class="empty-text">
                        No account claims are available.
                    </p>

                @endif

            </section>

            <!-- ======================================
                 ROLES
            ======================================= -->

            <section class="section-card">

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

                            <path d="m12 3 8 4v5c0 5-3.5 8-8 9-4.5-1-8-4-8-9V7l8-4Z"/>
                            <path d="m9 12 2 2 4-4"/>

                        </svg>

                    </div>

                    <div>
                        <h2>${messages.get("profile.roles")}</h2>
                        <p>Roles assigned to your account</p>
                    </div>

                </header>

                @if (!roles.isEmpty())

                    <ul class="tag-list">

                        @for (var role : roles)

                            <li class="tag">

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

                                ${role}

                            </li>

                        @endfor

                    </ul>

                @else

                    <p class="empty-text">
                        No roles are assigned to your account.
                    </p>

                @endif

            </section>

            <!-- ======================================
                 PERMISSIONS
            ======================================= -->

            <section class="section-card">

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

                            <rect x="3" y="11" width="18" height="10" rx="2"/>
                            <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
                            <circle cx="12" cy="16" r="1"/>

                        </svg>

                    </div>

                    <div>
                        <h2>${messages.get("profile.permissions")}</h2>
                        <p>Actions your account is authorized to perform</p>
                    </div>

                </header>

                @if (!permissions.isEmpty())

                    <ul class="tag-list">

                        @for (var permission : permissions)

                            <li class="tag permission-tag">

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

                                ${permission}

                            </li>

                        @endfor

                    </ul>

                @else

                    <p class="empty-text">
                        No permissions are available.
                    </p>

                @endif

            </section>

            <!-- ======================================
                 PROFILE NAVIGATION
            ======================================= -->

            <section class="navigation-section">

                <h2 class="navigation-title">
                    Account Management
                </h2>

                <nav class="navigation-grid" aria-label="Account management">

                    <!-- EDIT PROFILE -->

                    <a class="navigation-link" href="/profile/edit">

                        <span class="navigation-icon">

                            <svg
                                    viewBox="0 0 24 24"
                                    fill="none"
                                    stroke="currentColor"
                                    stroke-width="1.8"
                                    stroke-linecap="round"
                                    stroke-linejoin="round"
                                    aria-hidden="true">

                                <path d="M12 20h9"/>
                                <path d="M16.5 3.5a2.1 2.1 0 0 1 3 3L9 17l-4 1 1-4Z"/>

                            </svg>

                        </span>

                        <span class="navigation-text">
                            ${messages.get("profile.edit")}
                        </span>

                    </a>

                    <!-- CHANGE PASSWORD -->

                    <a class="navigation-link" href="/profile/password">

                        <span class="navigation-icon">

                            <svg
                                    viewBox="0 0 24 24"
                                    fill="none"
                                    stroke="currentColor"
                                    stroke-width="1.8"
                                    stroke-linecap="round"
                                    stroke-linejoin="round"
                                    aria-hidden="true">

                                <rect x="3" y="11" width="18" height="10" rx="2"/>
                                <path d="M7 11V7a5 5 0 0 1 10 0v4"/>

                            </svg>

                        </span>

                        <span class="navigation-text">
                            ${messages.get("profile.changePassword")}
                        </span>

                    </a>

                    <!-- LINKED ACCOUNTS -->

                    <a class="navigation-link" href="/profile/accounts">

                        <span class="navigation-icon">

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

                        </span>

                        <span class="navigation-text">
                            ${messages.get("profile.linkedAccounts")}
                        </span>

                    </a>

                    <!-- CONNECTED APPLICATIONS -->

                    <a class="navigation-link" href="/profile/apps">

                        <span class="navigation-icon">

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

                        </span>

                        <span class="navigation-text">
                            ${messages.get("profile.connectedApps")}
                        </span>

                    </a>

                </nav>

            </section>

            <!-- ======================================
                 LOGOUT
            ======================================= -->

            <form
                    class="logout-form"
                    method="post"
                    action="/logout">

                <input
                        type="hidden"
                        name="${csrfParameterName}"
                        value="${csrfToken}">

                <button
                        class="logout-btn"
                        type="submit">

                    <svg
                            viewBox="0 0 24 24"
                            fill="none"
                            stroke="currentColor"
                            stroke-width="1.8"
                            stroke-linecap="round"
                            stroke-linejoin="round"
                            aria-hidden="true">

                        <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/>
                        <path d="M16 17l5-5-5-5"/>
                        <path d="M21 12H9"/>

                    </svg>

                    ${messages.get("common.logout")}

                </button>

            </form>

            <!-- LANGUAGE SWITCHER -->

            <div class="language-section">

                @template.partials.languageSwitcher(messages = messages)

            </div>

        </main>

    </section>

</div>

</body>
</html>