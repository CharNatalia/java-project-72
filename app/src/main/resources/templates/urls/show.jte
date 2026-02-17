@import hexlet.code.dto.urls.UrlPage
@import hexlet.code.util.Formatter
@import hexlet.code.util.NamedRoutes
@param UrlPage page

@template.layout.page(
content = @`
        <main class="flex-grow-1">
            @if(page != null && page.getMessage() != null)
                <div class="rounded-0 m-0 alert alert-dismissible fade show alert-danger" role="alert">
                    ${page.getMessage()}
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            @endif
            <section>
                <div class="container-lg mt-5">
                    <h1>Сайт: ${page.getUrl().getName()}</h1>
                    <table class="table table-bordered table-hover mt-3">
                        <tbody>
                        <tr>
                            <td>ID</td>
                            <td>${page.getUrl().getId()}</td>
                        </tr>

                        <tr>
                            <td>Имя</td>
                            <td>${page.getUrl().getName()}</td>
                        </tr>

                        <tr>
                            <td>Дата создания</td>
                            <td>${Formatter.formatedDate(page.getUrl().getCreatedAt())}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </section>

            <section>
                <div class="container-lg mt-5">
                    <h2 class="mt-5">Проверки</h2>
                    
                    <form action="${NamedRoutes.urlCheckPath(page.getUrl().getId())}" method="post" class="rss-form text-body">
                        <button type="submit" class="btn btn-primary">Запустить проверку</button>
                    </form>
                    
                    <table class="table table-bordered table-hover mt-3">
                        <thead>
                            <tr>
                                <th class="col-1">ID</th>
                                <th class="col-1">Код ответа</th>
                                <th>title</th>
                                <th>h1</th>
                                <th>description</th>
                                <th  class="col-2">Дата проверки</th>
                            </tr>
                        </thead>
                        @if(page != null && page.getChecks() != null)
                            @for (var check : page.getChecks())
                                <tbody>
                                <tr>
                                    <td>${check.getId()}</td>
                                    <td>${check.getStatusCode()}</td>
                                    <td>${check.getTitle()}</td>
                                    <td>${check.getH1()}</td>
                                    <td>${check.getDescription()}</td>
                                    <td>${Formatter.formatedDate(check.getCreatedAt())}</td>
                                </tr>
                                </tbody>
                            @endfor
                        @endif
                    </table>
                </div>
            </section>
        </main>
`
)
