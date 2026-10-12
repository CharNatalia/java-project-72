@import hexlet.code.dto.urls.UrlPage
@import hexlet.code.util.Formatter
@import hexlet.code.util.NamedRoutes

@param UrlPage page

@template.layout.page(
page = page,
content = @`
    <div class="mx-auto max-w-7xl px-4 py-10">
        <h1 class="mb-4 text-4xl font-normal">
            Сайт: ${page.getUrl().getName()}
        </h1>

        <table class="w-full border-collapse text-left" data-test="url">
            <tbody>
            <tr>
                <th class="w-1/2 border border-gray-300 p-2 font-normal">
                    ID
                </th>
                <td class="border border-gray-300 p-2">
                    ${page.getUrl().getId()}
                </td>
            </tr>
            <tr>
                <th class="border border-gray-300 p-2 font-normal">
                    Имя
                </th>
                <td class="border border-gray-300 p-2">
                    ${page.getUrl().getName()}
                </td>
            </tr>
            <tr>
                <th class="border border-gray-300 p-2 font-normal">
                    Дата создания
                </th>
                <td class="border border-gray-300 p-2">
                    ${Formatter.formatedDate(page.getUrl().getCreatedAt())}
                </td>
            </tr>
            </tbody>
        </table>

        <section class="mt-12">
            <h2 class="mb-2 text-3xl">Проверки</h2>

            <form action="${NamedRoutes.urlCheckPath(page.getUrl().getId())}" method="post" class="mb-4">
                <input type="submit" value="Запустить проверку"
                       class="cursor-pointer rounded bg-blue-600 px-3 py-2 text-white hover:bg-blue-700">
            </form>

            <div class="overflow-x-auto">
                <table class="w-full border-collapse text-left" data-test="checks">
                    <thead>
                    <tr>
                        <th class="border border-gray-300 p-2">ID</th>
                        <th class="border border-gray-300 p-2">Код ответа</th>
                        <th class="border border-gray-300 p-2">h1</th>
                        <th class="border border-gray-300 p-2">title</th>
                        <th class="border border-gray-300 p-2">description</th>
                        <th class="border border-gray-300 p-2">Дата проверки</th>
                    </tr>
                    </thead>
                    @if (page.getUrlChecks() != null)
                        @for (var check : page.getUrlChecks())
                            <tbody>
                            <tr>
                                <td class="border border-gray-300 p-2">${check.getId()}</td>
                                <td class="border border-gray-300 p-2">${check.getStatusCode()}</td>
                                <td class="border border-gray-300 p-2">${check.getH1()}</td>
                                <td class="border border-gray-300 p-2">${check.getTitle()}</td>
                                <td class="border border-gray-300 p-2">${check.getDescription()}</td>
                                <td class="whitespace-nowrap border border-gray-300 p-2">
                                    ${Formatter.formatedDate(check.getCreatedAt())}
                                </td>
                            </tr>
                            </tbody>
                        @endfor
                    @endif
                </table>
            </div>
        </section>
    </div>

`
)