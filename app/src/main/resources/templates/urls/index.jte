@import hexlet.code.dto.urls.BuildUrlsPage
@import hexlet.code.util.NamedRoutes
@param BuildUrlsPage page = null

@template.layout.page(
content = @`
    <main class="flex-grow-1">
        @if(page != null && page.getMessage() != null)
        <div class="rounded-0 m-0 alert alert-${page.getAlertType()}
         d-flex justify-content-between align-items-center fade show" role="alert">
            ${page.getMessage()}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
        @endif

        <section>
            <div class="container-lg mt-5">
                <h1>Сайты</h1>
                <table class="table table-bordered table-hover mt-3">
                    <thead>
                    <tr>
                        <th class="col-1">ID</th>
                        <th>Имя</th>
                        <th class="col-2">Последняя проверка</th>
                        <th class="col-1">Код ответа</th>
                    </tr>
                    </thead>
                    @if(page != null && !page.getUrls().isEmpty())
                        <tbody>
                        <tr>
                            @for (var url : page.getUrls())
                                <td>${url.getId()}</td>
                                <td><a href="${NamedRoutes.urlPath(url.getId())}">${url.getName()}</a></td>
                                <td></td>
                                <td></td>
                            @endfor
                        </tr>
                        </tbody>
                    @endif
                </table>
            </div>
        </section>
    </main>
`
)