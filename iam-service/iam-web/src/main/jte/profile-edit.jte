@param java.util.Map<String, String> messages
@param String csrfToken
@param String csrfParameterName

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>${messages.get("profile.edit")}</title>
</head>
<body>
    <main>
        <h1>${messages.get("profile.edit")}</h1>

        <form method="post" action="/profile/edit">
            <input type="hidden" name="${csrfParameterName}" value="${csrfToken}">

            <label>
                ${messages.get("common.givenName")}
                <input name="givenName">
            </label>

            <label>
                ${messages.get("common.familyName")}
                <input name="familyName">
            </label>

            <label>
                ${messages.get("profile.nickname")}
                <input name="nickname">
            </label>

            <label>
                ${messages.get("profile.locale")}
                <select name="locale">
                    <option value="en">${messages.get("common.english")}</option>
                    <option value="km">${messages.get("common.khmer")}</option>
                </select>
            </label>

            <button>${messages.get("common.submit")}</button>
        </form>

        <p>
            <a href="?lang=en">${messages.get("common.english")}</a>
            |
            <a href="?lang=km">${messages.get("common.khmer")}</a>
        </p>
    </main>
</body>
</html>