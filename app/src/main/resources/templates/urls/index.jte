@import hexlet.code.dto.urls.UrlsPage
@import hexlet.code.repository.UrlCheckRepository
@import hexlet.code.util.Formatter
@import hexlet.code.util.NamedRoutes

@param UrlsPage page

@template.layout.page(
content = @`
    <div class="mx-auto max-w-7xl px-4 py-10">
        <h1 class="mb-4 text-4xl font-normal">
            Сайты
        </h1>

        <section class="mt-12">
            <div class="overflow-x-auto">
                <table class="border-collapse text-left" data-test="urls">
                    <thead>
                    <tr>
                        <th class="w-[10%] border border-gray-300 p-2 align-bottom">ID</th>
                        <th class="w-[60%] border border-gray-300 p-2 align-bottom">Имя</th>
                        <th class="w-[20%] border border-gray-300 p-2 align-bottom">Последняя проверка</th>
                        <th class="w-[10%] border border-gray-300 p-2 align-bottom">Код проверки</th>
                    </tr>
                    </thead>
                    <tbody>
                    @for (var check : page.getUrlWithLastUrlsChecks())
                        <tr>
                            <td class="w-[10%] border border-gray-300 p-2">${check.getUrlId()}</td>
                            <td class="w-[60%] border border-gray-300 p-2">
                                <a
                                        href="${NamedRoutes.urlPath(check.getUrlId())}"
                                        class="text-blue-600 underline hover:text-blue-800">${check.getName()}
                                </a>
                            </td>
                            @if (check.getStatus() != null && check.getCreatedAt() != null)
                                <td class="w-[20%] border border-gray-300 p-2 whitespace-nowrap">
                                    ${Formatter.formatedDate(check.getCreatedAt())}
                                </td>
                                <td class="w-[10%] border border-gray-300 p-2">
                                    ${check.getStatus()}
                                </td>
                            @else
                                <td class="w-[20%] border border-gray-300 p-2"></td>
                                <td class="w-[10%] border border-gray-300 p-2"></td>
                            @endif

                        </tr>
                    @endfor
                    </tbody>
                </table>
            </div>
        </section>
    </div>

`
)