package predictor.twitter;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Created by JunjieW on 2015/12/14.
 */
public class SentimentAnalyst {

    public static void main(String[] args) throws IOException {
        String text = "Cascada is a no-show, Flo Rida was good, and Kelly Clarkson is next! Still waiting for my Boom Boom Pow....\n" +
                "\"Boom Boom Pow - Black Eyed Peas :) â™? .\n" +
                "Zahara Boom Boom Pow!! .\n" +
                "My little sister, Janett, is teaching me her dance to \"Boom Boom Pow\" by the Black Eyed Peas. LOL.\n" +
                "For anyone that didn't download my Boom Boom Pow ReBoot, get it here ---> .\n" +
                "Echa un vistazo a este vÃ­deo. -- Boom Boom Pow - Black Eyed Peas (Official HQ) .\n" +
                "Black Eyed Peas - Boom Boom Pow .\n" +
                "Boom Boom Pow! Quem sÃ£o meus seguidores??.\n" +
                "@jaonyourmind We live in a time where your worst single ever can be #1! Boom Boom Pow is #1, Are u tellin me this is BEP's best song?.\n" +
                "#amazon #mp3 #track #1: Boom Boom Pow: Boom Boom Pow by Black Eyed Peas From the Album: The E.. .\n" +
                "#amazon #mp3 #track #2: Boom Boom Pow: Boom Boom Pow by Black Eyed Peas 4 days in the top 100.. .\n" +
                "Black Eyed Peas - Boom Boom Pow .\n" +
                "Jeffree Star - Boom Boom Pow (Black Eye Peas Remix).\n" +
                "Straks pÃ¥ Radioplanet: Justin Timberlake - If I, Kid Cudi - Poke Her Face og Black Eyed Peas - Boom Boom Pow! .\n" +
                "Black Eyed Peas - Boom Boom Pow .\n" +
                "The Billboard Hot 100 #1 This Week The Black Eyed Peas <a href=\"Boom Pow</a>.\n" +
                "I refuse to listen to Boom Boom Pow. I think that's the most annoying song ever made..\n" +
                "if I hear Boom Boom Pow ONE more time...\n" +
                "'s favorite song is \"Boom Boom Pow\" - Black Eyed Peas!.\n" +
                "I was just thinking about the black eyed peas, then I turn on my radio and Boom Boom Pow was on ! Weirrd.\n" +
                "@julianax02 Haha quite the family! Your mom sings Boom Boom Pow and your sis sings Dirty Little Secret in the middle of a store, LOL. Haha..\n" +
                "\"We be rocking them beats..\" - Black Eyes Peas - Boom Boom Pow .\n" +
                "Black Eyed Peas - Boom Boom Pow [Jimmy Kimmel Live] (2009 ... .\n" +
                "Boom Boom Pow Remix Pack Mashup by Omriki. check it out: .\n" +
                "RT @CeLine_XD: the \" Boom Boom Pow \" music video is stupidly lame. omg I totally agree on that!.\n" +
                "Boom Boom Pow to you @Walls101: \"booooooooom!\" â™? .\n" +
                "@caycatastrophe I'm surprised I haven't gotten sick of Boom Boom Pow yet. I can't help but dance along haha -- Fergie's voice is fierce.\n" +
                "Boom Boom Pow! =).\n" +
                "I'm on that Boom Boom Pow!!! .\n" +
                "\"Boom Boom Pow\" is so catchy. I believe Will*I*Am is a \"stunt double\" though..feel me?.\n" +
                "\"Beats so big Iâ€™m steppin on a leprachaun\" - Black Eyes Peas-Boom Boom Pow .\n" +
                "WOW, I'm so tired I'm hiper as crazy! How sad is that!? Jeez. What's up with all this \"Boom Boom Pow\" stuff? It's lame!.\n" +
                "I Got That Boom Boom Pow - LOVING #sytycd - - check it out, dance along, rate, comment â™?.\n" +
                "Black Eyed Peas Perform â€˜Boom Boom Powâ€? & More On â€˜Todayâ€? .\n" +
                "#youtube - D4F Contest Oberhausen: Solo Adults - Boom Boom Pow .\n" +
                "Black Eyed Peasâ€“Boom Boom Pow This song goes out to @Lefreak78 who is probably sitting poolside right now dr... â™? .\n" +
                "Boom Boom Pow - Black Eyed Peas .\n" +
                "I woke up dancing today. No really, Boom Boom Pow was on, and I started dancing in my bed. It's going to be a good day..\n" +
                "@zerbetron Black Eyed Peas â€? Boom Boom Pow Rmx Ft @50cent â™? .\n" +
                "Boom Boom Pow- Black eyed peas.\n" +
                "RT @iFrogz: LOVE this! Check out @Adroadtrip video to \"Boom Boom Pow\" starring the iFrogz EarPollution headphones: .\n" +
                "#musicmonday :: Black Eyed Peas - Boom Boom Pow :: I like that boom boom pow...I'm on the next ish now ::.\n" +
                "My latest CD acquisitions, got these via Brother in Law :-) Black Eye Peas - Boom Boom Pow from The E.N.D #mu... â™? .\n" +
                "Black Eyed Peas - Boom Boom Pow .\n" +
                "Geez. Boss' car, off to lunch. Being subjected to Boom Boom Pow. I'm not hungry..\n" +
                "Black Eyed Peas â€? Boom Boom Pow â™? .\n" +
                "#musicmonday Black Eyed Peas - Boom Boom Pow .\n" +
                "I got that Boom Boom Pow!.\n" +
                "@austinprime did u play Boom Boom Pow yet?? I went on my 30 min walk!! Don't know if i missed it..\n" +
                "Can I get some Boom Boom Pow and one egg roll.\n" +
                "RT @PaulCubbyBryant: Can I get some Boom Boom Pow and one egg roll?.\n" +
                "RT @PaulCubbyBryant Can I get some Boom Boom Pow and one egg roll.\n" +
                "Now I'm gonna make a veggie burger (no bun) and spinach! Like Fergie said \"Boom Boom Pow...I'm on that next shit now\"!!! I'm focused!!!.\n" +
                "Finally Just Uploaded My Boom Boom Pow Video On YouTube! Check It Out! .\n" +
                "love black eyed peas? check out @djeloy's \"Boom Boom Pow\" video edit! #musicmondays PLS RT.\n" +
                "Ã‰ o som do momento, mas sÃ³ a 1a metade. Na 2a, o som cai. Que remixes corrijam isso. Black Eyed Peas, Boom Boom Pow. â™? .\n" +
                "Reading @metrolyrics Boom Boom Pow #lyrics (Black Eyed Peas) - .\n" +
                "\"Boom Boom Pow - Black Eyed Peas :) â™? .\n" +
                "@NikiScherzinger Boom Boom PowW!!! heller miss nicole i'm your biggest fan i'll follow u around until u love me hehe i dont know what 2 say.\n" +
                "Hey in that song, Boom Boom Pow, it sounds like they are saying Boom Boom Boom. ... Read More: .\n" +
                "@vprincess You have to wait until Stacy Ferguson (a.k.a. Fergie) starts singing. She says, \"I like that Boom Boom Pow\". .\n" +
                "Black Eyed Peas - Boom Boom Pow .\n" +
                "just discovered Jeffree Star's versoin of Boom Boom Pow. hehe woh!.\n" +
                "Itâ€™s So 3000 and 8: , Bring your Boom Boom Pow to Wednesday night at Starline for another installment of t.. .\n" +
                "Black Eyed Peas - Boom Boom Pow .\n" +
                "What better song to get ready for the tea party then Boom Boom Pow by Black Eyed Peas? .\n" +
                "*â™«Black Eyed Peas - Boom Boom Pow (75 Brazil Street/Calle Ocho Mega Mix) CUTE.\n" +
                "Heeeeeel hard douchen en dansen lalalala met Boom Boom Pow van de Black Eyed Peas (een beroerde groep, maar wel lekker dansen).\n" +
                "\"Boom Boom Pow\" is a song by the Black Eyed Peas released as the lead single from their upcoming album The E.N.D.. .\n" +
                "Boom Boom Pow.\n" +
                "Boom Boom Pow! Great home in Miramar, look! We love Smart Living! Great time to buy! .\n" +
                "Just for you @farmgirl60 shake it up girl DanceDanceDance Black Eyed Peas - Boom Boom Pow .\n" +
                "No1 - Today's Top MP3 Songs, Boom Boom Pow by Black Eyed Peas, .\n" +
                "Updated my blog with Motivating Music: Boom Boom Pow! .\n" +
                "Aight gimme 10 8counts Go! RT @BlackMamba23: This is \"Boom Boom Pow\" is perfect for a Pom Pon routine...bout to revisit my Parkdale Pom days.\n" +
                "Aight gimme 10 8counts Go! RT @BlackMamba23: This is \"Boom Boom Pow\" is perfect for a Pom Pon routine...bout to revisit my Parkdale Pom days.\n" +
                "Let's Boom Boom Pow this morning! â™? .\n" +
                "Black Eyed Peas â€? Boom Boom Pow â™? .\n" +
                "added 'Boom Boom Pow' by Black Eyed Peas to the 'yvette's hooked up' playlist @imeem .\n" +
                "Black Eyed Peas - Boom Boom Pow .\n" +
                "RT @IngridRS: Boom Boom Pow boom boom pow to you too:-).\n" +
                "Black Eyed Peas â€? Boom Boom Pow â™? .\n" +
                "#iranradio GET OUT OF YOUR SEAT and DANCE WITH ME! Black Eyed Peas \"Boom Boom Pow\" â™? #iranelection.\n" +
                "Hey Look! It's @g_harrelljr 's song! RT @TwistenFM: Hot on Twitter: \"Boom Boom Pow\" by Black Eyed Peas - .\n" +
                "Isn't Boom Boom Pow an annoying song :|.\n" +
                "I got that Boom Boom Pow, those chickens jackin' my style! Ha!.\n" +
                "Boom Boom Pow! RT @PleasureEllis: The Black Eyed Peas, \"The E.N.D.\": The Black Eyed Peas, \"The E.N.D.\" .\n" +
                "NP: *Boom Boom Pow - Black Eyes Peas* (music) .\n" +
                "@Alexagreen siii sacÃ³ una canciÃ³n mÃ¡s buena se llama Boom Boom Pow.\n" +
                "@bep Hey made a new video for Boom Boom Pow for you, hope you like it, woof woof. .\n" +
                "RT @TwistenFM Hot on Twitter: \"Boom Boom Pow\" by Black Eyed Peas - .\n" +
                "RT @TwistenFM Hot on Twitter: \"Boom Boom Pow\" by Black Eyed Peas - .\n" +
                "WittyVideos.Net: Boom Boom Pow Remix By Dr Dave: This is my version of the Black Eyed Peas video.. .\n" +
                "@elizabethdawn Boom Boom Pow by Black Eyed Peas.\n" +
                "â™? Boom Boom Pow by Black Eyed Peas #lastfm: amazon: .\n" +
                "Black Eyed Peas and Lady Gaga each have 2 songs in the Top 10! BEP Got A Feeling #1 and Boom Boom Pow #2; Gaga Lovegame #4 and Pokerface #9.\n" +
                "LA Championship Celebration @ The Coliseum. Da only songs played Boom Boom Pow and Day n Nite(Crookers mix). Stadium Status!!!!!!.\n" +
                "RT @chrishalloran: @jeremycowart Black Eyed Peas Boom Boom Pow is a fantastic workout song.\n" +
                "I just noticed that the version of \"Boom Boom Pow\" on Kiss 108 has the \"satellite radio\" lyrics removed. That seems kind of paranoid..\n" +
                "Twitteraoke Day 2 : Black Eyed Peas - Boom Boom Pow - (volume will be louder tomorrow) PLEASE RT!!.\n" +
                "Name That Track Answer: Boom Boom Pow - Black Eyed Peas.\n" +
                "RT : @andrewwp Twitteraoke Day 2 : Black Eyed Peas - Boom Boom Pow - (volume will be louder tomorrow).\n" +
                "RT @UMG_News: B.E.P. and Lady Gaga each have 2 songs in Top 10! BEP Got A Feeling #1 and Boom Boom Pow #2; Gaga Lovegame #4 and Pokerface #9.\n" +
                "The Black Eyed Peas -Boom Boom Pow I freaking love this sonnnng :).\n" +
                "Get The Black Eyed Peas \"Boom Boom Pow\" ringtones and much more .\n" +
                "Black Eyed Peas - Boom Boom Pow .\n" +
                "Get The Black Eyed Peas \"Boom Boom Pow\" ringtones and much more .\n" +
                "RT @kenissomewhere If there's a bit of a Boom Boom Pow shortage it's cause I bought it all and put it on Ebay. .\n" +
                "En ce moment sur Hotmixradio Hits: THE BLACK EYED PEAS - Boom Boom Pow (Dirty Club)... .\n" +
                "Get The Black Eyed Peas \"Boom Boom Pow\" ringtones and much more .\n" +
                "Having Jeffree Star's version of Boom Boom Pow stuck in your head while doing a history final doesnt exactly help with concentration.\n" +
                "@saragarth - Boom Boom Pow - LOL. :P - Will upload Riot Act later, it sounds all pretty :) x.\n" +
                "RT @305cutie @Teflon305 sure do I drank all n it was good <---- Boy I can't wait until I catch yo ass in da streets!! Boom Boom Pow!.\n" +
                "Black Eyed Peas Take Top Two Slots On Billboard Hot 100: After eleven weeks at No. 1 with \"Boom Boom Pow,\" the B.. .\n" +
                "Reading @metrolyrics Boom Boom Pow #lyrics (Black Eyed Peas) - .\n" +
                "808, 3008, Super-8, stupid 8-bit: A closer look at the language and cultural references of Boom Boom Pow. .\n" +
                "I Nabbed &#9835; Boom Boom Pow by Black Eyed Peas &#9835;. Get your own Nabbs at www.nabbit.com. #nabbit.\n" +
                "RT @mix961playlist Black Eyed Peas - Boom Boom Pow.\n" +
                "Black Eyed Peas - Boom Boom Pow .\n" +
                "Just Added: \"Boom Boom Pow\" by Black Eyed Peas .\n" +
                "Europe Official Top chart #1 Boom Boom Pow - Black Eyed Peas#free download .\n" +
                "Love this track: Black Eyed Peas \"Boom Boom Pow\" .\n" +
                "Black Eyed Peas - Boom Boom Pow .\n" +
                "RT @Fringemunks congrats to #BlackEyedPeas for having the #1 and #2 songs (\"Boom Boom Pow\" / \"I Gotta Feeling\") on the Billboard Hot 100.\n" +
                "Black Eyed Peas - Boom Boom Pow - 02:06 PM visit www.RadioTAGr.com/WNYC to TAG this song.\n" +
                "â–? Black Eyed Peas - 'Boom Boom Pow'...\"I'm so 3008, you're so 2000 and LATE!\".\n" +
                "RT @White_Ace: @MarciaPCF did you listen Jeffree Star - 'Boom Boom Pow'?? Oh MY! Am I wrong for kinda liking it lol?! :) lol xo'.\n" +
                "Boom Boom Pow â™? .\n" +
                "Boom Boom Pow sounds like the thunder I hear outside: â™? .\n" +
                "\"Black Eyed Peas\" - 'Boom Boom Pow'http://www.youtube.com/watch?v=9F444CELomo.\n" +
                "Posted a new song: \"Boom Boom Pow Rmx - Ft 50 Cent- Hit by jLome\" .\n" +
                "@BarbieBrittania You know it!! I'm bringing that Boom Boom Pow tonight! lol.\n" +
                "Posted a new song: \"Boom Boom Pow - Black Eye Peas feat. TyBless Joe Bells Remix\" .\n" +
                "- @bep Boom Boom Pow @Vfest Mtl!!.\n" +
                "Cant wait!! RT @TheComputerNerd: spending all night editing the Boom Boom Pow parody.. I'll be posting it around 1PM EST. Tomorrow :).\n" +
                "Boom Boom Pow.\n" +
                "RT @nadeemd: @nadeemd: \"Black Eyed Peas â€? Boom Boom Pow\" â™? Thanks for the Follow Friday.\n" +
                "@fauxhemian Boom Boom Pow? That I don't get..\n" +
                "Original Boom Boom Pow Lyrics Black Eyed Peace Video Clips #music .\n" +
                "Posted a new song: \"Boom Boom Pow - Black Eye Peas feat. TyBless and Giovanni Joe Bells Remix\" .\n" +
                "The boy is back ;~) Playing rt now Black Eyed Peas - Boom Boom Pow .\n" +
                "#iranradio Black Eyed Peas \"Boom Boom Pow\" â™? #iranelection.\n" +
                "Boom Boom Pow Parody (( Sham Wow Now )) Is now on Youtube .\n" +
                "New blog post: Boom Boom Pow - Black Eyed Peas Parody ( Sham Wow Now ) .\n" +
                "Boom Boom Pow in McDonough, GA .\n" +
                "Vamu pra radio galera?? Tocando Boom Boom Pow (David Guetta Remix) - Black Eyed Peas PeÃ§am sua musik q eu toco lah \\o/.\n" +
                "how do you come up with the most random stuff lol RT @TheComputerNerd Boom Boom Pow Parody Sham Wow Now .\n" +
                "Top Rated - Boom Boom Pow - Black Eyed Peas Parody ( Sham Wow Now ) .\n" +
                "#adamrock Boom Boom Pow - Black Eyed Peas Parody ( Sham Wow Now ) .\n" +
                "@Ethan_Ecstasy Boom Boom Pow xD.\n" +
                "3 of the best from......Black Eyed Peas - #1 - RB: @JODYGIRL162 - Boom Boom Pow (Ornique's 7\" Ultimix) â™? .\n" +
                "Song of the Current (Part 3: Electric Boogaloo) - Black Eyed Peas - Boom Boom Pow .\n" +
                "Pedido das @MUlherzinhas agora (Tifany): Boom Boom Pow - Black Eyed Peas cola aew.\n";
        Properties props = new Properties();
        props.setProperty("annotators","tokenize, ssplit, pos, lemma, parse, sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        Annotation annotation = pipeline.process(text);
        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
            System.out.println(sentiment + "\t" + sentence);
        }
    }
}
