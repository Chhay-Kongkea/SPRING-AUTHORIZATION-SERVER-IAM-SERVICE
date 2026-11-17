@param kh.edu.istad.iam.web.i18n.Messages messages
@param String language
@param java.util.Map<String, String> values
@param String errorKey
@param String csrfToken
@param String csrfParameterName

<!doctype html>
<html lang="${language}">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="theme-color" content="#6d28d9">

    <title>${messages.get("profile.edit")} | IAM Platform</title>

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

        button,
        input,
        select {
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
           RIGHT SIDE - FORM
        ======================================== */

        .auth-panel {
            display: flex;
            justify-content: center;
            align-items: flex-start;
            padding: 50px 35px;
            background: #ffffff;
        }

        .form-card {
            width: 100%;
            max-width: 520px;
        }

        .form-header {
            margin-bottom: 27px;
        }

        .form-header h1 {
            margin-bottom: 10px;
            font-size: 34px;
            font-weight: 800;
            letter-spacing: -1.2px;
        }

        .form-header p {
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
            margin-bottom: 22px;
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
           FORM
        ======================================== */

        .profile-form {
            display: flex;
            flex-direction: column;
            gap: 19px;
        }

        .form-group {
            display: flex;
            flex-direction: column;
            gap: 8px;
        }

        .form-group label {
            color: #39354a;
            font-size: 13px;
            font-weight: 700;
        }

        .required-mark {
            color: var(--danger);
            margin-left: 3px;
        }

        .form-control {
            width: 100%;
            min-height: 49px;
            padding: 12px 14px;
            border: 1px solid var(--border);
            border-radius: 9px;
            outline: none;
            background: #ffffff;
            color: var(--text);
            font-size: 13px;
            transition:
                    border-color 0.2s ease,
                    box-shadow 0.2s ease;
        }

        .form-control::placeholder {
            color: #aaa5b8;
        }

        .form-control:hover {
            border-color: #c4b5fd;
        }

        .form-control:focus {
            border-color: var(--primary);
            box-shadow: 0 0 0 4px rgba(124, 58, 237, 0.1);
        }

        select.form-control {
            cursor: pointer;
            appearance: none;
            background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='%2377738a' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3E%3Cpath d='m6 9 6 6 6-6'/%3E%3C/svg%3E");
            background-repeat: no-repeat;
            background-position: right 14px center;
            padding-right: 40px;
        }

        .field-hint {
            color: var(--muted);
            font-size: 11px;
            line-height: 1.6;
        }

        /* ========================================
           FORM BUTTONS
        ======================================== */

        .form-actions {
            display: flex;
            flex-direction: column;
            gap: 13px;
            margin-top: 7px;
        }

        .save-btn {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 10px;
            width: 100%;
            min-height: 50px;
            border: none;
            border-radius: 9px;
            background: linear-gradient(
                    135deg,
                    var(--primary),
                    var(--primary-dark)
            );
            color: #ffffff;
            font-size: 14px;
            font-weight: 700;
            cursor: pointer;
            box-shadow: 0 5px 14px rgba(124, 58, 237, 0.2);
            transition:
                    transform 0.2s ease,
                    box-shadow 0.2s ease;
        }

        .save-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 20px rgba(124, 58, 237, 0.3);
        }

        .save-btn svg {
            width: 18px;
            height: 18px;
        }

        .back-link {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
            min-height: 45px;
            border: 1px solid var(--border);
            border-radius: 9px;
            background: #ffffff;
            color: var(--muted);
            font-size: 13px;
            font-weight: 600;
            text-decoration: none;
            transition: all 0.2s ease;
        }

        .back-link:hover {
            border-color: #c4b5fd;
            background: #faf8ff;
            color: var(--primary);
        }

        .back-link svg {
            width: 17px;
            height: 17px;
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
        button:focus-visible,
        input:focus-visible,
        select:focus-visible {
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

            .form-card {
                max-width: 550px;
            }
        }

        @media (max-width: 500px) {
            .form-header h1 {
                font-size: 29px;
            }

            .profile-form {
                gap: 16px;
            }
        }

        @media (max-width: 380px) {
            .auth-panel {
                padding: 30px 16px;
            }

            .form-header h1 {
                font-size: 26px;
            }

            .banner-content h2 {
                font-size: 30px;
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
                Personal Information
            </span>

            <h2>Make Your Identity Yours.</h2>

            <p>
                Keep your personal information up to date.
                Manage your profile details and customize your
                account preferences in one secure place.
            </p>

        </div>

        <div class="banner-footer">
            © 2026 IAM Platform. All rights reserved.
        </div>

    </section>

    <!-- ======================================
         RIGHT SIDE: EDIT PROFILE FORM
    ======================================= -->

    <section class="auth-panel">

        <main class="form-card">

            <header class="form-header">

                <h1>
                    ${messages.get("profile.edit")}
                </h1>

                <p>
                    Update your personal information and account preferences.
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

            <!-- PROFILE FORM -->

            <form
                    class="profile-form"
                    method="post"
                    action="/profile/edit">

                <input
                        type="hidden"
                        name="${csrfParameterName}"
                        value="${csrfToken}">

                <!-- GIVEN NAME -->

                <div class="form-group">

                    <label for="givenName">
                        ${messages.get("common.givenName")}
                        <span class="required-mark">*</span>
                    </label>

                    <input
                            class="form-control"
                            type="text"
                            id="givenName"
                            name="givenName"
                            value="${values.getOrDefault("givenName", "")}"
                            autocomplete="given-name"
                            required
                            maxlength="100">

                </div>

                <!-- FAMILY NAME -->

                <div class="form-group">

                    <label for="familyName">
                        ${messages.get("common.familyName")}
                        <span class="required-mark">*</span>
                    </label>

                    <input
                            class="form-control"
                            type="text"
                            id="familyName"
                            name="familyName"
                            value="${values.getOrDefault("familyName", "")}"
                            autocomplete="family-name"
                            required
                            maxlength="100">

                </div>

                <!-- NICKNAME -->

                <div class="form-group">

                    <label for="nickname">
                        ${messages.get("profile.nickname")}
                    </label>

                    <input
                            class="form-control"
                            type="text"
                            id="nickname"
                            name="nickname"
                            value="${values.getOrDefault("nickname", "")}"
                            autocomplete="nickname"
                            maxlength="100">

                </div>

                <!-- PHONE NUMBER -->

                <div class="form-group">

                    <label for="phoneNumber">
                        ${messages.get("profile.phone")}
                    </label>

                    <input
                            class="form-control"
                            type="tel"
                            id="phoneNumber"
                            name="phoneNumber"
                            value="${values.getOrDefault("phoneNumber", "")}"
                            placeholder="+85512345678"
                            autocomplete="tel">

                </div>

                <!-- LOCALE -->

                <div class="form-group">

                    <label for="locale">
                        ${messages.get("profile.locale")}
                    </label>

                    <select
                            class="form-control"
                            id="locale"
                            name="locale">

                        <option
                                value="en"
                                selected="${"en".equals(values.get("locale"))}">

                            ${messages.get("common.english")}

                        </option>

                        <option
                                value="km"
                                selected="${"km".equals(values.get("locale"))}">

                            ${messages.get("common.khmer")}

                        </option>

                    </select>

                    <p class="field-hint">
                        Choose the language preference for your account.
                    </p>

                </div>

                <!-- FORM ACTIONS -->

                <div class="form-actions">

                    <button
                            class="save-btn"
                            type="submit">

                        <svg
                                viewBox="0 0 24 24"
                                fill="none"
                                stroke="currentColor"
                                stroke-width="2"
                                stroke-linecap="round"
                                stroke-linejoin="round"
                                aria-hidden="true">

                            <path d="M19 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h11l5 5v11a2 2 0 0 1-2 2Z"/>
                            <path d="M17 21v-8H7v8"/>
                            <path d="M7 3v5h8"/>

                        </svg>

                        ${messages.get("common.save")}

                    </button>

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

                </div>

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