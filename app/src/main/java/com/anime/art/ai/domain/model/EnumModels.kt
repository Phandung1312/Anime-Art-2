package com.anime.art.ai.domain.model

enum class Sampler(val display: String, val sampler: String) {
    Random(display = "Random", sampler = ""),
    Ddim(display = "ddim", sampler = "ddim"),
    Dpm(display = "dpm", sampler = "dpm"),
    Euler(display = "euler", sampler = "euler"),
    EulerA(display = "euler_a", sampler = "euler_a")
}

enum class Ratio(val display: String, val ratio: String, val width: String, val height: String, val aspectRatio: Float) {
    Ratio1x1(display = "1x1", ratio = "1:1", width = "512", height = "512", aspectRatio = 1f / 1f),
    Ratio9x16(display = "9x16", ratio = "9:16", width = "324", height = "576", aspectRatio = 9f / 16f),
    Ratio16x9(display = "16x9", ratio = "16:9", width = "576", height = "324", aspectRatio = 16f / 9f),
    Ratio2x3(display = "2x3", ratio = "2:3", width = "340", height = "510", aspectRatio = 2f / 3f),
    Ratio3x2(display = "3x2", ratio = "3:2", width = "510", height = "340", aspectRatio = 3f / 2f),
    Ratio3x4(display = "3x4", ratio = "3:4", width = "384", height = "512", aspectRatio = 3f / 4f),
    Ratio4x3(display = "4x3", ratio = "4:3", width = "512", height = "384", aspectRatio = 4f / 3f),
}

enum class NumberOfImages(var display: String, val number: Int){
    NumberOfImages1(display = "1", number = 1),
    NumberOfImages2(display = "2", number = 2),
    NumberOfImages3(display = "3", number = 3),
    NumberOfImages4(display = "4", number = 4),
    NumberOfImages5(display = "5", number = 5),
    NumberOfImages6(display = "6", number = 6),
    NumberOfImages7(display = "7", number = 7),
    NumberOfImages8(display = "8", number = 8)
}

enum class Character(val characterName : String,val  promptText : String){
    Baby( characterName = "Baby", promptText = "3D art of a boy having small bearded for game avatar profile, " +
            "sf, intricate artwork masterpiece, ominous, matte painting movie poster, golden ratio, trending on cgsociety, intricate, epic, trending on artstation, " +
            "by artgerm, h. r. giger and beksinski, " +
            "highly detailed, vibrant, production cinematic character render, ultra high quality model"),
    Fairy( characterName = "Fairy", promptText = "masterpiece, best quality, anime, beautiful elf girl, " +
            "otherworldly plants, glowing plants, watercolor, colorful, " +
            "whimsical, fairy lights, little fairies, liquid light, otherworldly landscape, otherworldly liquid, " +
            "fantasia, abstract background, sharp focus, highly detailed, 8k"),
    Angle( characterName = "Angle", promptText = "Cinematic photography, angel: ana de armas, heavenly, 3/4 view, thick thighs, stockings, raining, lightning, dark fantasy, " +
            "hyper realistic, dynamic lighting, symmetry, 4k, desktop background, " +
            "sharp, intricate, ultra detailed, concept art, hdr 4k, 8k, " +
            "highly detailed, intricate, sharp focus, volumetric lights, volumetric fog, depth of field, f/1. 8, 85mm, symmetry"),
    OfficeGirl( characterName = "Office Girl", promptText = "beautiful realistik white woman with a perfect face, an enchanting smile, who works as a software developer in the office, " +
            "beautiful face, 8k, best quality, super detailed skin and face, dark illumination, stock picture, with blur background, 50mm F1.2, 16:9"),
    DemonGirl( characterName = "Demon Girl", promptText = "photo of 1 beautiful woman, demon, " +
            "portait,extremely detailed beautiful face, (demon horns 1:3), beautiful detailed eyes, smug, long hair| silver hair,off-shoulder leather clothes,black suit," +
            "(demon wings),(at night),forest,fire from hell,burnibg roses,best quality,masterpiece," +
            "extremely detailed CG unity 8k wallpaper,highly detailed, sharp focus,(backlight:1.3),rim light , art by greg rulkowski,award winning photograph,gothic,UE render"),
}

enum class SizeOfImage(val size : String, val describeImage : String, val realSize : String ){
    OneOne(size = "1:1", describeImage = "size_1_1", realSize = "1024:1024"),
    NineSixteen(size = "9:16", describeImage = "size_9_16", realSize = "1024:576"),
    SixteenNine(size = "16:9", describeImage = "size_16_9", realSize = "576:1024"),
    TwoThree(size = "2:3", describeImage = "size_2_3", realSize = "768:512"),
    ThreeTwo(size = "3:2", describeImage = "size_3_2", realSize = "512:768")
}

enum class ArtStyle(val artStyleName : String, val sourceImage : String,val  model :String){
    DarkSushi(artStyleName = "Dark Sushi", sourceImage = "dark_sushi", model = "dark-sushi-mix-v2-25"),
    ICBINP_Afterburn(artStyleName = "ICBINP Afterburn", sourceImage = "icbinp_afterburn", model = "icbinp-afterburn"),
//    Openjourney(artStyleName = "Openjourney", sourceImage = "openjourney", model = "openjourney-v1-0"),
    MoonFilmReality(artStyleName = "MoonFilm Reality", sourceImage = "moonfilm_reality", model = "moonfilm-reality-v3"),
    MoonFilmFilmGrain(artStyleName = "MoonFilm FilmGrain", sourceImage = "moonflim_filmgrain", model = "moonfilm-film-grain-v1"),
    MoonFilmUtopia(artStyleName = "MoonFilm Utopia", sourceImage = "moonflim_utopia", model = "moonfilm-utopia-v3"),
    SynthwavePunk(artStyleName = "SynthwavePunk", sourceImage = "synthwave_punk", model = "synthwave-punk-v2"),
//    StableXL(artStyleName = "StableXL", sourceImage = "stable_xl", model = "stable-diffusion-xl-v1-0"),
    Arcane(artStyleName = "Arcane", sourceImage = "arcane", model = "arcane-diffusion"),
    RealisticVision(artStyleName = "Realistic Vision", sourceImage = "realistic_vision", model = "realistic-vision-v3"),
    AbsoluteReality(artStyleName = "Absolute Reality", sourceImage = "absolute_reality", model = "absolute-reality-v1-6"),
    ICBINPFinal(artStyleName = "ICBINP Final", sourceImage = "icbinp_final", model = "icbinp-final"),
    ICBINPRealapse(artStyleName = "ICBINP Realapse", sourceImage = "icbinp_relapse", model = "icbinp-relapse"),
    InteriorDesign(artStyleName = "Interior Design", sourceImage = "interior_design", model = "xsarchitectural-interior-design"),
    ModernDisney(artStyleName = "Modern Disney", sourceImage = "modern_disney", model = "mo-di-diffusion"),
    RPG(artStyleName = "RPG", sourceImage = "rpg", model = "anashel-rpg"),
    Something(artStyleName = "Something", sourceImage = "something", model = "something-v2-2"),
    ICBINP(artStyleName = "ICBINP", sourceImage = "icbinp", model = "icbinp"),
    StableDiffusion(artStyleName = "Stable Diffusion", sourceImage = "stable_diffusion", model = "stable-diffusion-v1-5"),
    VangoghDiffusion(artStyleName = "Van Gogh Diffusion", sourceImage = "van_gogh_diffusion", model = "van-gogh-diffusion"),
    AnalogDiffusion(artStyleName = "Analog Diffusion", sourceImage = "analog_diffusion", model = "analog-diffusion"),
}

enum class ControlNet(val controlNetName : String, val sourceImage : String,val apiString : String){
    Canny(controlNetName = "ControlNet Canny", sourceImage = "controlnet_canny", apiString = "canny-1.1"),
    HardEdges(controlNetName = "ControlNet_Hard Edges", sourceImage = "controlnet_hardedges", apiString = "mlsd-1.1"),
    SoftEdges(controlNetName = "ControlNet_SoftEdges", sourceImage = "controlnet_softedges", apiString = "softedge-1.1"),
    LineartAnime(controlNetName = "ControlNet_LineartAnime", sourceImage = "controlnet_lineartanime", apiString = "lineart-anime-1.1"),
    NormalMap(controlNetName = "ControlNet_Normal Map", sourceImage = "controlnet_normalmap", apiString = "normal-1.1"),
    Dept(controlNetName = "ControlNet_Dept", sourceImage = "controlnet_dept", apiString = "depth-1.1"),
    Face(controlNetName = "ControlNet_Face", sourceImage = "controlnet_face", apiString = "mediapipeface"),
    Pose(controlNetName = "ControlNet_Pose", sourceImage = "controlnet_pose", apiString = "openpose-1.1"),
    FullBody(controlNetName = "ControlNet_Full Body", sourceImage = "controlnet_fullbody", apiString = "openpose-full-1.1"),
    Scribble(controlNetName = "ControlNet_Scribble", sourceImage = "controlnet_scribble", apiString = "scribble-1.1"),
    Lines(controlNetName = "ControlNet_Straight Lines", sourceImage = "controlnet_straightlines", apiString = "lineart-1.1"),
}

enum class SamplingMethod(val display : String ,val apiString: String){
    EulerA(display = "Euler a", apiString = "euler_a"),
    Euler(display = "Euler", apiString = "euler"),
    LMS(display = "LMS", apiString = "lms"),
    PNMDM(display = "PNDM", apiString = "pndm"),
    DPMSloverPlus(display = "DPMSlover++", apiString = "dpmsolver++"),
    DDIM(display = "DDIM", apiString = "ddim"),
}

enum class CharacterAppearance(val id : Int, val display : String){
    Object(id = 1, "Object"),
    Body(id = 2, "Body"),
    HairAndFace(id = 3, "Hair&Face"),
    Clothing(id = 4, "Clothing"),
    View(id = 5, "View"),
    Scence(id = 6, "Scence"),
    Action(id = 7, "Acion")
}

enum class Tag(val cAId : Int, val display : String ){
    OneGirl(cAId = 1, display = "1 girl"),
    Idol(cAId = 1, display = "Idol"),
    Bride(cAId = 1, display = "Bride"),
    Fairy(cAId = 1, display = "Fairy"),
    Godness(cAId = 1, display = "Godness"),
    Body(cAId = 1, display = "Body"),
    Queen(cAId = 1, display = "Queen"),
    OneBoy(cAId = 1, display = "1 boy"),
    Elf(cAId = 1, display = "Elf"),

    Collarbone(cAId = 2, display = "Collarbone"),
    Legs(cAId = 2, display = "Legs"),
    Muscular(cAId = 2, display = "muscular"),
    Abs(cAId = 2, display = "abs"),
    FairyWings(cAId = 2, display = "fairy wings"),
    Navel(cAId = 2, display = "Navel"),
    Anklet(cAId = 2, display = "anklet"),
    Back(cAId = 2, display = "back"),
    Thighs(cAId = 2, display = "thighs"),

    BloneHair(cAId = 3, display = "Blone Hair"),
    HeadWings(cAId = 3, display = "head wings"),
    BunnyEars(cAId = 3, display = "bunny ears"),
    RedLips(cAId = 3, display = "Red lips"),
    Longeyalshes(cAId = 3, display = "longeyalshes"),
    RoundEvewear(cAId = 3, display = "round evewears"),
    BridalVeil(cAId = 3, display = "bridal veil"),
    Stud(cAId = 3, display = "stud"),
    Earings(cAId = 3, display = "earings"),

    Bikini(cAId = 4, display = "Bikini"),
    Sportswear(cAId = 4, display = "sportswear"),
    Miniskirt(cAId = 4, display = "miniskirt"),
    Suit(cAId = 4, display = "suit"),
    JK(cAId = 4, display = "JK"),
    ThighStrap(cAId = 4, display = "thigh strap"),
    LaceBra(cAId = 4, display = "lace bra"),
    SailorSuit1(cAId = 4, display = "sailor suit"),
    WeddingDress(cAId = 4, display = "wedding dress"),

    LookingBack(cAId = 5, display = "looking back"),
    FromBehind(cAId = 5, display = "from behind"),
    Perspective(cAId = 5, display = "perspective"),
    FullBody(cAId = 5, display = "full body"),
    CloseUp(cAId = 5, display = "close-up"),
    SailorSuit2(cAId = 5, display = "sailor suit"),
    FocusOnFace(cAId = 5, display = "Focus on face"),

    Bed(cAId = 6, display = "Bed"),
    InSpring(cAId = 6, display = "in spring"),
    Fireworks(cAId = 6, display = "fireworks"),
    FullMoon(cAId = 6, display = "full-moon"),
    Sea(cAId = 6, display = "sea"),
    Playground(cAId = 6, display = "playground"),

    Pout(cAId = 7, display = "Pout"),
    WalkTheDog(cAId = 7, display = "walk the dog"),
    PrincessCarry(cAId = 7, display = "princess carry"),
    Shushing(cAId = 7, display = "shushing"),
    CatPose(cAId = 7, display = "cat pose"),
    Stretch(cAId = 7, display = "stretch"),
}

enum class DailyReward(val reward : String){
    Day1( reward = "10"),
    Day2( reward = "10"),
    Day3( reward = "15"),
    Day4( reward = "15"),
    Day5( reward = "15"),
    Day6( reward = "20"),
    Day7( reward = "20"),
}

enum class CreditPackage(val credit : Int){
    First(credit = 500),
    Second(credit = 1000),
    Third(credit = 2000),
    Fourth(credit = 5000),
    Fifth(credit = 10000),
}

