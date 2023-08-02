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
    OfficeGirl( characterName = "OfficeGirl", promptText = "beautiful realistik white woman with a perfect face, an enchanting smile, who works as a software developer in the office, " +
            "beautiful face, 8k, best quality, super detailed skin and face, dark illumination, stock picture, with blur background, 50mm F1.2, 16:9"),
    DemonGirl( characterName = "DemonGirl", promptText = "photo of 1 beautiful woman, demon, " +
            "portait,extremely detailed beautiful face, (demon horns 1:3), beautiful detailed eyes, smug, long hair| silver hair,off-shoulder leather clothes,black suit," +
            "(demon wings),(at night),forest,fire from hell,burnibg roses,best quality,masterpiece," +
            "extremely detailed CG unity 8k wallpaper,highly detailed, sharp focus,(backlight:1.3),rim light , art by greg rulkowski,award winning photograph,gothic,UE render"),
}

enum class SizeOfImage(val size : String, val describeImage : String, val realSize : String ){
    OneOne(size = "1:1", describeImage = "size_1_1", realSize = "1024:1024"),
    NineSix(size = "9:6", describeImage = "size_9_6", realSize = "1024:576"),
    SixteenNine(size = "16:9", describeImage = "size_16_9", realSize = "576:1024"),
    TwoThree(size = "2:3", describeImage = "size_2_3", realSize = "768:512"),
    ThreeTwo(size = "3:2", describeImage = "size_3_2", realSize = "512:768")
}

enum class ArtStyle(val artStyleName : String, val sourceImage : String,val  model :String){
    AGG(artStyleName = "AGG", sourceImage = "agg", model = "absolute-reality-v1-6"),
    Realistic(artStyleName = "Realistic", sourceImage = "realistic", model = "realistic-vision-v3"),
    PencilSketch(artStyleName = "PencilSketch", sourceImage = "pencil_sketch", model = "stable-diffusion-v1-5"),
    SuperHero(artStyleName = "SuperHero", sourceImage = "super_hero", model = "anashel-rpg"),
    ZFlighters(artStyleName = "ZFlighters", sourceImage = "z_flighters", model = "stable-diffusion-v1-5"),
    PastelColors(artStyleName = "PastelColors", sourceImage = "pastel_colors", model = "stable-diffusion-v1-5"),
    Ninja(artStyleName = "Ninja", sourceImage = "ninja", model = "stable-diffusion-v1-5"),
    FantasicJOJO(artStyleName = "FantasicJOJO", sourceImage = "fantasic_jojo", model = "stable-diffusion-v1-5"),
    Tarot(artStyleName = "Tarot", sourceImage = "tarot", model = "stable-diffusion-v1-5"),
    NinetyComic(artStyleName = "_90sComic", sourceImage = "_90s_comic", model = "stable-diffusion-v1-5"),
    ColorIllustration(artStyleName = "ColorIllustration", sourceImage = "color_illustration", model = "stable-diffusion-v1-5"),
    GameCG(artStyleName = "GameCG", sourceImage = "game_cg", model = "stable-diffusion-v1-5"),
    Wild(artStyleName = "Wild", sourceImage = "wild", model = "stable-diffusion-v1-5"),
    Asian(artStyleName = "Asian", sourceImage = "asian", model = "stable-diffusion-v1-5"),
    InkAndWask(artStyleName = "InkAndWask", sourceImage = "ink_and_wask", model = "stable-diffusion-v1-5"),
    Chibi(artStyleName = "Chibi", sourceImage = "chibi", model = "stable-diffusion-v1-5"),
    Cyberpunk(artStyleName = "Cyberpunk", sourceImage = "cyberpunk", model = "stable-diffusion-v1-5"),
}

enum class ControlNet(val controlNetName : String, val sourceImage : String,val apiString : String){
    Canny(controlNetName = "ControlNet Canny", sourceImage = "controlnet_canny", apiString = "canny-1.1"),
    Hed(controlNetName = "ControlNet_Hed", sourceImage = "controlnet_hed", apiString = "canny-1.1"),
    Openpose(controlNetName = "ControlNet_Openpose", sourceImage = "controlnet_openpose", apiString = "openpose-1.1"),
    Scribble(controlNetName = "ControlNet_Scribble", sourceImage = "controlnet_scribble", apiString = "scribble-1.1"),
    HardEdges(controlNetName = "ControlNet_Hard Edges", sourceImage = "controlnet_hard_edges", apiString = "canny-1.1"),
    SoftEdges(controlNetName = "ControlNet_SoftEdges", sourceImage = "controlnet_soft_edges", apiString = "canny-1.1"),
    LineartAnime(controlNetName = "ControlNet_LineartAnime", sourceImage = "controlnet_linear_anime", apiString = "lineart-anime-1.1"),
    Face(controlNetName = "ControlNet_Face", sourceImage = "controlnet_face", apiString = "canny-1.1")
}

enum class SamplingMethod(val display : String , apiString: String){
    EulerA(display = "Euler A", apiString = "euler_a"),
    Euler(display = "Euler", apiString = "euler"),
    LMS(display = "LMS", apiString = "lms"),
    DMP2(display = "DPM2", apiString = "euler_a"),
    DPM2a(display = "DPM2 a", apiString = "euler_a"),
    DPMPlus2Sa(display = "DPM++2S a", apiString = "euler_a"),
    DPMPlusSDE(display = "DPM++ SDE", apiString = "euler_a"),
    DPMFast(display = "DPM fast", apiString = "euler_a"),
    DPMAdaptive(display = "DPM adaptive", apiString = "euler_a"),
    LMSAKarras(display = "LMS Karras", apiString = "euler_a"),
    DPM2Karras(display = "DPM2 Karras", apiString = "euler_a"),
    DPM2aKarras(display = "DPM a Karras", apiString = "euler_a"),
    DPMPlus2SaKarras(display = "DPM++2S a Karras", apiString = "euler_a"),
    DPMPlus2MaKarras(display = "DPM++2MaKarras", apiString = "euler_a"),
    DPMPlusSDEKarras(display = "DPM++SDE Karras", apiString = "euler_a"),
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
    Fairy(cAId = 1, display = "Fairy"),
    Body(cAId = 1, display = "Body"),
    Queen(cAId = 1, display = "Queen"),
    Elf(cAId = 1, display = "Elf"),
    Idol(cAId = 1, display = "Idol"),
    Bride(cAId = 1, display = "Bride"),
    Godness(cAId = 1, display = "Godness"),
    OneBoy(cAId = 1, display = "OneBoy"),
    Collarbone(cAId = 2, display = "Collarbone"),
    Abs(cAId = 2, display = "abs"),
    Navel(cAId = 2, display = "Navel"),
    Anklet(cAId = 2, display = "anklet"),
    Thighs(cAId = 2, display = "thighs"),
    Legs(cAId = 2, display = "Legs"),
    Muscular(cAId = 2, display = "muscular"),
    FairyWings(cAId = 2, display = "fairy wings"),
    Back(cAId = 2, display = "back"),
    BloneHair(cAId = 3, display = "Blone Hair"),
    BunnyEars(cAId = 3, display = "bunny ears"),
    RoundEvewear(cAId = 3, display = "round evewears"),
    Stud(cAId = 3, display = "stud"),
    Earings(cAId = 3, display = "earings"),
    HeadWings(cAId = 3, display = "head wings"),
    RedLips(cAId = 3, display = "Red lips"),
    Longeyalshes(cAId = 3, display = "longeyalshes"),
    BridalVeil(cAId = 3, display = "bridal veil"),
    Bikini(cAId = 4, display = "Bikini"),
    Miniskirt(cAId = 4, display = "miniskirt"),
    ThighStrap(cAId = 4, display = "thigh strap"),
    SailorSuit1(cAId = 4, display = "sailor suit"),
    WeddingDress(cAId = 4, display = "wedding dress"),
    Sportswear(cAId = 4, display = "sportswear"),
    Suit(cAId = 4, display = "suit"),
    JK(cAId = 4, display = "JK"),
    LaceBra(cAId = 4, display = "lace bra"),
    LookingBack(cAId = 5, display = "looking back"),
    FullBody(cAId = 5, display = "full body"),
    CloseUp(cAId = 5, display = "close-up"),
    SailorSuit2(cAId = 5, display = "sailor suit"),
    FocusOnFace(cAId = 5, display = "Focus on face"),
    FromBehind(cAId = 5, display = "from behind"),
    Perspective(cAId = 5, display = "perspective"),
    Bed(cAId = 6, display = "Bed"),
    Fireworks(cAId = 6, display = "fireworks"),
    FullMoon(cAId = 6, display = "full-moon"),
    Sea(cAId = 6, display = "sea"),
    Playground(cAId = 6, display = "playground"),
    InSpring(cAId = 6, display = "in spring"),
    Pout(cAId = 7, display = "Pout"),
    PrincessCarry(cAId = 7, display = "princess carry"),
    Shushing(cAId = 7, display = "shushing"),
    CatPose(cAId = 7, display = "cat pose"),
    Stretch(cAId = 7, display = "stretch"),
    WalkTheDog(cAId = 7, display = "walk the dog"),
}

enum class DailyReward(val reward : String){
    Day1( reward = "10"),
    Day2( reward = "20"),
    Day3( reward = "30"),
    Day4( reward = "50"),
    Day5( reward = "70"),
    Day6( reward = "80"),
    Day7( reward = "100"),
}

