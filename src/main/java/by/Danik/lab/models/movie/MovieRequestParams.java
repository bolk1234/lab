package by.Danik.lab.models.movie;

/**
 * Параметры запроса на сервер для поиска фильма по названию
 */
public class MovieRequestParams {
    private String title;
    private int page;
    private int limit;

    public MovieRequestParams(String title, int page, int limit) {
        this.title = title;
        this.page = page;
        this.limit = limit;
    }

    public String getTitle() {
        return title;
    }

    public int getPage() {
        return page;
    }

    public int getLimit() {
        return limit;
    }
}