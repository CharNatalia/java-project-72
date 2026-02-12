@import hexlet.code.dto.urls.UrlPage
@param UrlPage page

@template.layout.page(
content = @`
        <main class="flex-grow-1">
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
                            <td>${page.getUrl().formatedDate()}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </section>

            <section>
                <div class="container-lg mt-5">
                    <h2 class="mt-5">Проверки</h2>
                    <button type="button" class="btn btn-primary">Запустить проверку</button>
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
                    </table>
                </div>
            </section>
        </main>
`
)
