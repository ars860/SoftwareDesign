import java.util.List;
import java.util.Objects;

public class SearchEngineResponseRow {
    public String title;
    public String link;
    public String description;

    public SearchEngineResponseRow(String title, String link, String description) {
        this.title = title;
        this.link = link;
        this.description = description;
    }

    public String print() {
        return "Title: \"" + title + "\" link: \"" + link + "\"\n" + "Body: \"" + description + "\"";
    }

    static public String printList(List<SearchEngineResponseRow> list) {
        return list.stream().map(SearchEngineResponseRow::print).reduce(String::concat).orElse("");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchEngineResponseRow that = (SearchEngineResponseRow) o;

        if (!Objects.equals(title, that.title)) return false;
        if (!Objects.equals(link, that.link)) return false;
        return Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        result = 31 * result + (link != null ? link.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }
}
