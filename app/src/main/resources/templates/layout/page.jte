@import gg.jte.Content
@import hexlet.code.dto.BasePage
@param Content content
@param BasePage page = null

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Javalin Blog</title>
    <link href="/css/main.css" rel="stylesheet">
</head>

<body class="flex min-h-screen flex-col bg-gray-50 text-gray-900">
<nav class="flex flex-wrap items-center gap-6 bg-gray-900 px-4 py-3">
    <a href="/" class="text-xl text-white">
        Анализатор страниц
    </a>
    <a href="/" class="text-gray-400 hover:text-white">
        Главная
    </a>
    <a href="/urls" class="text-gray-400 hover:text-white">
        Сайты
    </a>
</nav>
@if (page != null && page.getFlash() != null)
    !{var flashClass = "success".equals(page.getFlashType())
            ? "border-green-200 bg-green-50 text-green-800"
            : "danger".equals(page.getFlashType())
                ? "border-red-200 bg-red-50 text-red-800"
                : "border-sky-200 bg-sky-50 text-sky-800";}
    <div class="flex border-b px-4 py-3 ${flashClass}" role="alert">
        <p class="max-w-5xl">${page.getFlash()}</p>
    </div>
@endif
<main class="flex-1 px-4 py-3">
    ${content}
</main>

<footer class="border-t border-gray-200 bg-white py-4">
    <div class="mx-auto max-w-5xl px-4 text-center text-sm text-gray-600">
        created by
        <a class="text-blue-600 hover:underline" href="https://github.com/CharNatalia" target="_blank">Natalia</a>
    </div>
</footer>
</body>
</html>