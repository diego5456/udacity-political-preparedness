import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DateJsonAdapter {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    @FromJson
    fun dateFromJson(date: String): Date? {
        return try {
            dateFormat.parse(date)
        } catch (e: ParseException) {
            null
        }
    }

    @ToJson
    fun dateToJson(date: Date): String {
        return dateFormat.format(date)  // Ensure the date is formatted to "yyyy-MM-dd"
    }
}