import net.fortuna.ical4j.data.CalendarBuilder
import net.fortuna.ical4j.model.*
import java.io.ByteArrayInputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.stream.Collectors
import java.util.stream.IntStream


//import java.util.Calendar


fun main(args: Array<String>) {
    println("Hello World!")

    val curYear = 2024
    val calendarGenerator = CalendarGenerator(curYear)
    val calendar = calendarGenerator.build()

    println(calendar.toString())


    println("Program arguments: ${args.joinToString()}")

}
class CalendarGenerator(val currentYear: Int) {
    val booksPlan = getPlan()
    val booksMapping = getBookMapping()
    val dateformatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    val weekdays = IntStream
        .rangeClosed(1, LocalDate.ofYearDay(currentYear + 1, 1).minusDays(1).dayOfYear)
        .mapToObj { day -> LocalDate.ofYearDay(currentYear, day) }
        .filter { date -> date.getDayOfWeek().getValue() <= 5 }
        .collect(Collectors.toList())

     fun build(): Calendar {
         val stringBuilder= StringBuilder()
        var ndx = 0

        do {
            val date = weekdays[ndx]
            val formattedDate = dateformatter.format(date)
            println("generating for ndx ${ndx} and date ${formattedDate}")
            stringBuilder.append("\n")
            stringBuilder.append(buildEvent(formattedDate, ndx))
        } while (++ndx < MAX_DAYS)
        stringBuilder.append("\n")
        val builder = CalendarBuilder()
        val parsed = builder.build(ByteArrayInputStream((vCalBegin + stringBuilder.toString() + vCalEnd).toByteArray()))
        parsed.validate()
        return parsed
    }

    fun buildEvent(
        dateString: String,
        eventNumber: Int,
    ): String {
        val bookAndChapter = booksPlan[eventNumber]
        val linkUri = getYouVersionUri(bookAndChapter)
        val webUri = getWebUri(bookAndChapter)
        val bookName = booksMapping.get(bookAndChapter.book)

        val description =
            "Day ${eventNumber+1}: ${bookName} Chapter ${bookAndChapter.chapter}.<br>  Open on <a href=\"${linkUri}\"><u>YouVersion</u></a> <br>  Open on <a href=\"${webUri}\"><u>Web</u></a>"

        val vEventTemplate = """
        BEGIN:VEVENT
        DTSTART;VALUE=DATE:${dateString}
        DTEND;VALUE=DATE:${dateString}
        UID:${UID}+eventNumber
        CLASS:PUBLIC
        DESCRIPTION:${description}
        SEQUENCE:0
        STATUS:CONFIRMED
        SUMMARY: Bible Reading Plan Day ${eventNumber+1}
        TRANSP:TRANSPARENT
        END:VEVENT
    """.trimIndent()

        return vEventTemplate
    }

    fun getYouVersionUri(bookAndChapter: BookAndChapter) : String {
        return "youversion://Bible?reference=${bookAndChapter.book}.${bookAndChapter.chapter}"
    }

    fun getWebUri(bookAndChapter: BookAndChapter) : String {
        return "https://www.bible.com/bible/111/${bookAndChapter.book}.${bookAndChapter.chapter}"
    }


    fun getBookMapping(): Map<String, String> {
        val threeAndNames = threeLetterToBookMapping.split("\n")
        val returnMap = mutableMapOf<String, String>()
        for (line in threeAndNames) {
            returnMap[line.substring(0, 3)] = line.substring(4)
        }
        return returnMap
    }

    data class BookAndChapter(val book: String, val chapter: Int)

    fun getPlan(): List<BookAndChapter> {
        return dailyPlan.split("\n")
            .map { line ->
                BookAndChapter(line.substring(0, 3), line.substring(4).toInt())
            }
    }

    companion object {

        val UID = "adlkflj1289adaj"

        val threeLetterToBookMapping = """
    GEN	Genesis
    EXO	Exodus
    LEV	Leviticus
    NUM	Numbers
    DEU	Deuteronomy
    JOS	Joshua
    JDG	Judges
    RUT	Ruth
    1SA	1 Samuel
    2SA	2 Samuel
    1KI	1 Kings
    2KI	2 Kings
    1CH	1 Chronicles
    2CH	2 Chronicles
    EZR	Ezra
    NEH	Nehemiah
    EST	Esther
    JOB	Job
    PSA	Psalms
    PRO	Proverbs
    ECC	Ecclesiastes
    SOS	Song of Solomon
    ISA	Isaiah
    JER	Jeremiah
    LAM	Lamentations
    EZE	Ezekiel
    DAN	Daniel
    HOS	Hosea
    JOE	Joel
    AMO	Amos
    OBA	Obadiah
    JON	Jonah
    MIC	Micah
    NAH	Nahum
    HAB	Habakkuk
    ZEP	Zephaniah
    HAG	Haggai
    ZEC	Zechariah
    MAL	Malachi
    MAT	Matthew
    MAR	Mark
    LUK	Luke
    JOH	John
    ACT	Acts
    ROM	Romans
    1CO	1 Corinthians
    2CO	2 Corinthians
    GAL	Galatians
    EPH	Ephesians
    PHP	Philippians
    COL	Colossians
    1TH	1 Thessalonians
    2TH	2 Thessalonians
    1TI	1 Timothy
    2TI	2 Timothy
    TIT	Titus
    PHM	Philemon
    HEB	Hebrews
    JAM	James
    1PE	1 Peter
    2PE	2 Peter
    1JO	1 John
    2JO	2 John
    3JO	3 John
    JDE	Jude
    REV	Revelation
""".trimIndent()

        val dailyPlan = """
        LUK 1
        LUK 2
        LUK 3
        LUK 4
        LUK 5
        LUK 6
        LUK 7
        LUK 8
        LUK 9
        LUK 10
        LUK 11
        LUK 12
        LUK 13
        LUK 14
        LUK 15
        LUK 16
        LUK 17
        LUK 18
        LUK 19
        LUK 20
        LUK 21
        LUK 22
        LUK 23
        LUK 24
        ACT 1
        ACT 2
        ACT 3
        ACT 4
        ACT 5
        ACT 6
        ACT 7
        ACT 8
        ACT 9
        ACT 10
        ACT 11
        ACT 12
        ACT 13
        ACT 14
        ACT 15
        ACT 16
        ACT 17
        ACT 18
        ACT 19
        ACT 20
        ACT 21
        ACT 22
        ACT 23
        ACT 24
        ACT 25
        ACT 26
        ACT 27
        ACT 28
        JAM 1
        JAM 2
        JAM 3
        JAM 4
        JAM 5
        GAL 1
        GAL 2
        GAL 3
        GAL 4
        GAL 5
        GAL 6
        1TH 1
        1TH 2
        1TH 3
        1TH 4
        1TH 5
        2TH 1
        2TH 2
        2TH 3
        PHM 1
        HEB 1
        HEB 2
        HEB 3
        HEB 4
        HEB 5
        HEB 6
        HEB 7
        HEB 8
        HEB 9
        HEB 10
        HEB 11
        HEB 12
        HEB 13
        MAR 1
        MAR 2
        MAR 3
        MAR 4
        MAR 5
        MAR 6
        MAR 7
        MAR 8
        MAR 9
        MAR 10
        MAR 11
        MAR 12
        MAR 13
        MAR 14
        MAR 15
        MAR 16
        ROM 1
        ROM 2
        ROM 3
        ROM 4
        ROM 5
        ROM 6
        ROM 7
        ROM 8
        ROM 9
        ROM 10
        ROM 11
        ROM 12
        ROM 13
        ROM 14
        ROM 15
        ROM 16
        1CO 1
        1CO 2
        1CO 3
        1CO 4
        1CO 5
        1CO 6
        1CO 7
        1CO 8
        1CO 9
        1CO 10
        1CO 11
        1CO 12
        1CO 13
        1CO 14
        1CO 15
        1CO 16
        2CO 1
        2CO 2
        2CO 3
        2CO 4
        2CO 5
        2CO 6
        2CO 7
        2CO 8
        2CO 9
        2CO 10
        2CO 11
        2CO 12
        2CO 13
        COL 1
        COL 2
        COL 3
        COL 4
        EPH 1
        EPH 2
        EPH 3
        EPH 4
        EPH 5
        EPH 6
        PHP 1
        PHP 2
        PHP 3
        PHP 4
        1TI 1
        1TI 2
        1TI 3
        1TI 4
        1TI 5
        1TI 6
        2TI 1
        2TI 2
        2TI 3
        2TI 4
        TIT 1
        TIT 2
        TIT 3
        1PE 1
        1PE 2
        1PE 3
        1PE 4
        1PE 5
        2PE 1
        2PE 2
        2PE 3
        JOH 1
        JOH 2
        JOH 3
        JOH 4
        JOH 5
        JOH 6
        JOH 7
        JOH 8
        JOH 9
        JOH 10
        JOH 11
        JOH 12
        JOH 13
        JOH 14
        JOH 15
        JOH 16
        JOH 17
        JOH 18
        JOH 19
        JOH 20
        JOH 21
        1JO 1
        1JO 2
        1JO 3
        1JO 4
        1JO 5
        2JO 1
        3JO 1
        JDE 1
        REV 1
        REV 2
        REV 3
        REV 4
        REV 5
        REV 6
        REV 7
        REV 8
        REV 9
        REV 10
        REV 11
        REV 12
        REV 13
        REV 14
        REV 15
        REV 16
        REV 17
        REV 18
        REV 19
        REV 20
        REV 21
        REV 22
        MAT 1
        MAT 2
        MAT 3
        MAT 4
        MAT 5
        MAT 6
        MAT 7
        MAT 8
        MAT 9
        MAT 10
        MAT 11
        MAT 12
        MAT 13
        MAT 14
        MAT 15
        MAT 16
        MAT 17
        MAT 18
        MAT 19
        MAT 20
        MAT 21
        MAT 22
        MAT 23
        MAT 24
        MAT 25
        MAT 26
        MAT 27
        MAT 28
    """.trimIndent()

        val vCalBegin = """
        BEGIN:VCALENDAR
        PRODID:-//Google Inc//Google Calendar 70.9054//EN
        VERSION:2.0
        CALSCALE:GREGORIAN
        METHOD:PUBLISH
        X-WR-CALNAME:Bible Study Calendar
        X-WR-TIMEZONE:America/Chicago
        BEGIN:VTIMEZONE
        TZID:America/Chicago
        X-LIC-LOCATION:America/Chicago
        BEGIN:DAYLIGHT
        TZOFFSETFROM:-0600
        TZOFFSETTO:-0500
        TZNAME:CDT
        DTSTART:19700308T020000
        RRULE:FREQ=YEARLY;BYMONTH=3;BYDAY=2SU
        END:DAYLIGHT
        BEGIN:STANDARD
        TZOFFSETFROM:-0500
        TZOFFSETTO:-0600
        TZNAME:CST
        DTSTART:19701101T020000
        RRULE:FREQ=YEARLY;BYMONTH=11;BYDAY=1SU
        END:STANDARD
        END:VTIMEZONE
    """.trimIndent()


        val vCalEnd = """
        END:VCALENDAR
    """.trimIndent()

        val MAX_DAYS = 260
    }
}




