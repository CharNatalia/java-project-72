@import gg.jte.Content
@import hexlet.code.util.NamedRoutes;
@param Content content

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4" crossorigin="anonymous"></script>
    <title>Hexlet Javalin</title>
</head>
<body class="d-flex flex-column min-vh-100">
<nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
        <a class="navbar-brand" href="${NamedRoutes.rootPath()}">Анализатор страниц</a>
        <button  class="navbar-toggler"
                 type="button"
                 data-bs-toggle="collapse"
                 data-bs-target="#navbarToggler"
                 aria-controls="navbarToggler"
                 aria-expanded="false"
                 aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse"
             id="navbarToggler">
               <ul class="navbar-nav">
                   <li class="nav-item">
                       <a class="nav-link" href="${NamedRoutes.rootPath()}">Главная</a>
                   </li>
                   <li class="nav-item">
                       <a class="nav-link" href="${NamedRoutes.urlsPath()}">Сайты</a>
                   </li>
               </ul>
        </div>
    </div>
</nav>
${content}
</body>

<footer class="footer border-top py-3 mt-5 bg-light">
    <div class="text-center">Сделала Наташа</div>
</footer>
</html>
