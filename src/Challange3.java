import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Challange3 {
    public static void main(String[] args) throws Exception{
        var apiKey = "<your_key>";
        var request = HttpRequest
                .newBuilder()
                .uri(URI.create("https://imdb-api.com/en/API/Top250Movies/" + apiKey))
                .GET()
                .build();

        var client = HttpClient.newHttpClient();

        var response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        var json = response.body();

        var movies = parseJsonMovies((json));

        System.out.println(movies);
    }

    private static List<Movie> parseJsonMovies(String json) {
        Matcher matcher = Pattern.compile(".*\\[(.*)\\].*").matcher(json);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("no match in " + json);
        }

        var moviesArray = matcher.group(1).split("\\},\\{");
        moviesArray[0] = moviesArray[0].substring(1);
        var last = moviesArray.length - 1;
        var lastString = moviesArray[last];
        moviesArray[last] = lastString.substring(0, lastString.length() - 1);


        var movieList = new ArrayList<Movie>();

        for (var movie : moviesArray) {
            var attributes = movie.split("\",");
            var titleField = attributes[2].substring(9);
            var urlImageField = attributes[5].substring(9);
            var ratingField = attributes[7].substring(14);
            var yearField = attributes[4].substring(8);

            movieList.add(new Movie(titleField, urlImageField, ratingField, yearField));
        }

        return movieList;
    }
}
