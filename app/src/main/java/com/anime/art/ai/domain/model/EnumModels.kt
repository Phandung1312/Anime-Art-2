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

enum class Character(val characterName : String){
    Baby( characterName = "Baby"),
    Fairy( characterName = "Fairy"),
    Angle( characterName = "Angle"),
    OfficeGirl( characterName = "OfficeGirl"),
    DemonGirl( characterName = "DemonGirl"),
}

enum class SizeOfImage(val size : String, val describeImage : String ){
    OneOne(size = "1:1", describeImage = "size_1_1"),
    NineSix(size = "9:6", describeImage = "size_9_6"),
    SixteenNine(size = "16:9", describeImage = "size_16_9"),
    TwoThree(size = "2:3", describeImage = "size_2_3"),
    ThreeTwo(size = "3:2", describeImage = "size_3_2")
}

enum class ArtStyle(val artStyleName : String, val sourceImage : String){
    AGG(artStyleName = "AGG", sourceImage = "agg"),
    Realistic(artStyleName = "Realistic", sourceImage = "realistic"),
    PencilSketch(artStyleName = "PencilSketch", sourceImage = "pencil_sketch"),
    SuperHero(artStyleName = "SuperHero", sourceImage = "super_hero"),
    ZFlighters(artStyleName = "ZFlighters", sourceImage = "z_flighters"),
    PastelColors(artStyleName = "PastelColors", sourceImage = "pastel_colors"),
    Ninja(artStyleName = "Ninja", sourceImage = "ninja"),
    FantasicJOJO(artStyleName = "FantasicJOJO", sourceImage = "fantasic_jojo"),
    Tarot(artStyleName = "Tarot", sourceImage = "tarot"),
    NinetyComic(artStyleName = "_90sComic", sourceImage = "_90s_comic"),
    ColorIllustration(artStyleName = "ColorIllustration", sourceImage = "color_illustration"),
    GameCG(artStyleName = "GameCG", sourceImage = "game_cg"),
    Wild(artStyleName = "Wild", sourceImage = "wild"),
    Asian(artStyleName = "Asian", sourceImage = "asian"),
    InkAndWask(artStyleName = "InkAndWask", sourceImage = "ink_and_wask"),
    Chibi(artStyleName = "Chibi", sourceImage = "chibi"),
    Cyberpunk(artStyleName = "Cyberpunk", sourceImage = "cyberpunk"),
}

enum class ControlNet(val controlNetName : String, val sourceImage : String){
    Canny(controlNetName = "ControlNet Canny", sourceImage = "controlnet_canny"),
    Hed(controlNetName = "ControlNet_Hed", sourceImage = "controlnet_hed"),
    Openpose(controlNetName = "ControlNet_Openpose", sourceImage = "controlnet_openpose"),
    Scribble(controlNetName = "ControlNet_Scribble", sourceImage = "controlnet_scribble"),
    HardEdges(controlNetName = "ControlNet_Hard Edges", sourceImage = "controlnet_hard_edges"),
    SoftEdges(controlNetName = "ControlNet_SoftEdges", sourceImage = "controlnet_soft_edges"),
    LineartAnime(controlNetName = "ControlNet_LineartAnime", sourceImage = "controlnet_linear_anime"),
    Face(controlNetName = "ControlNet_Face", sourceImage = "controlnet_face")
}

enum class SamplingMethod(val display : String ){
    EulerA(display = "Euler A"),
    Euler(display = "Euler"),
    LMS(display = "LMS"),
    DMP2(display = "DPM2"),
    DPM2a(display = "DPM2 a"),
    DPMPlus2Sa(display = "DPM++2S a"),
    DPMPlusSDE(display = "DPM++ SDE"),
    DPMFast(display = "DPM fast"),
    DPMAdaptive(display = "DPM adaptive"),
    LMSAKarras(display = "LMS Karras"),
    DPM2Karras(display = "DPM2 Karras"),
    DPM2aKarras(display = "DPM a Karras"),
    DPMPlus2SaKarras(display = "DPM++2S a Karras"),
    DPMPlus2MaKarras(display = "DPM++2MaKarras"),
    DPMPlusSDEKarras(display = "DPM++SDE Karras"),
    DDIM(display = "DDIM"),
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

