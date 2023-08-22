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
enum class SizeOfImage(val size : String, val describeImage : String, val width: String, val height: String, val aspectRatio: Float ){
    Ratio1x1(size = "1:1", describeImage = "size_1_1",width = "512", height = "512", aspectRatio = 1f / 1f),
    Ratio9x16(size = "9:16", describeImage = "size_9_16",  width = "288", height = "512", aspectRatio = 9f / 16f),
    Ratio16x9(size = "16:9", describeImage = "size_16_9",  width = "512", height = "288", aspectRatio = 16f / 9f),
    Ratio2x3(size = "2:3", describeImage = "size_2_3", width = "320", height = "480", aspectRatio = 2f / 3f),
    Ratio3x2(size = "3:2", describeImage = "size_3_2", width = "480", height = "320", aspectRatio = 3f / 2f)
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
    Fairy( characterName = "Fairy", promptText = "anime - style illustration of a woman dressed in a fairy costume, " +
            "fairy cgsociety, beautiful and elegant elf queen, fantasy style art, alluring elf princess knight, astral fairy, " +
            "fantasy art style, beautiful and elegant female elf, digital fantasy art ), elf queen nissa genesis mage, " +
            "beautiful elegant dryad, by Yang J, hyperdetailed fantasy character, beautiful fairy"),
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


enum class ArtStyle(val artStyleName : String, val sourceImage : String,val  model :String, val extraPrompt : String){
    DarkSushi(artStyleName = "Dark Sushi", sourceImage = "dark_sushi", model = "dark-sushi-25d", extraPrompt = "(solo)1.6, high quality images, "),
    RealisticVision(artStyleName = "Realistic Vision", sourceImage = "realistic_vision", model = "majic-mix-realistic", extraPrompt = "(solo)1.6, "),
    Spring(artStyleName = "Spring", sourceImage = "spring", model = "dark-sushi-25d", extraPrompt = "warm spring, fresh, lively, the flower field is in full bloom with bright colors, the branches are sprouting, flower-covered path, solo, "),
    DragonGirl(artStyleName = "Dragon Girl", sourceImage = "dragon_girl", model = "dark-sushi-25d", extraPrompt = "dragon girl, dragon horns, dragon wings, slit_pupils, pointy_ears, dragon tail, 1girl, solo, reptile_girl, masterpiece,fire, blazing, (highres), absurdres, 4K, 8K, 16K, hyper-detailed, deep eyes, close-up <lora:animeDragonGirl_v10:0.7>, "),
    Library(artStyleName = "Library", sourceImage = "library", model = "dark-sushi-25d", extraPrompt = "gentle and intellectual appearance, library space with bookshelves, books and natural light from the windows"),
    Christmas(artStyleName = "Christmas", sourceImage = "christmas", model = "dark-sushi-25d", extraPrompt = "girl, christmas, christmas tree,  fireplace, warm, solo, HDR 4K, 8K, "),
    Pool(artStyleName = "Swimming Pool", sourceImage = "swimming_pool", model = "dark-sushi-25d", extraPrompt = "in swimming pool, water, bikini, solo"),
    Ninja(artStyleName = "Ninja", sourceImage = "ninja", model = "dark-sushi-25d", extraPrompt = "(ninja)1.6, traditional ninja costume, precise weapon, anime character,  "),
    PencilSketch(artStyleName = "Pencil Sketch", sourceImage = "pencil_sketch", model = "majic-mix-realistic", extraPrompt = "illustration concept art, anime, manga, pencil sketch, pencil drawing, inking, black and white trending pixiv fanbox, art by ilya kuvshinov and ghibli, loose pencil sketch, sketchy, concept art, cinematic, white space, black and white, solo, "),
    SuperHero(artStyleName = "Super Hero", sourceImage = "super_hero", model = "majic-mix-realistic", extraPrompt = "photography and realistic lighting. Additionally, there is an impressive superhero portrait of SuperHero in a SuperHero costume. The 8k art germ bokeh, created by Wojtek Fuse, showcases remarkable digital illustration and realistic digital art in 4k. The close-up character portrait is highly detailed and displays a superb photo-realistic quality, "),
    Chibi(artStyleName = "Chibi", sourceImage = "chibi", model = "dark-sushi-25d", extraPrompt = "(little character)1.3, (chibi)1.3,full body, best quality, chibi, (solo)1.3, full character concept, "),
}

enum class ControlNet(val controlNetName : String, val sourceImage : String,val apiString : String){
    Canny(controlNetName = "ControlNet Canny", sourceImage = "controlnet_canny", apiString = "canny"),
    Hed(controlNetName = "ControlNet Hed", sourceImage = "controlnet_hed", apiString = "hed"),
    Mlsd(controlNetName = "ControlNet_Mlsd", sourceImage = "controlnet_mlsd", apiString = "mlsd"),
    Openpose(controlNetName = "ControlNet_Openpose", sourceImage = "controlnet_openpose", apiString = "openpose"),
    Segmentation(controlNetName = "ControlNet_Segmentation", sourceImage = "controlnet_segmentation", apiString = "segmentation"),
    Aesthetic(controlNetName = "ControlNet_Aesthetic", sourceImage = "controlnet_aesthetic", apiString = "aesthetic-controlnet"),
    Inpaint(controlNetName = "ControlNet_Inpaint", sourceImage = "controlnet_inpaint", apiString = "inpaint"),
    Shuffle(controlNetName = "ControlNet_Shuffle", sourceImage = "controlnet_shuffle", apiString = "shuffle"),
    Tile(controlNetName = "ControlNet_Tile", sourceImage = "controlnet_tile", apiString = "tile"),
    SoftEdges(controlNetName = "ControlNet_SoftEdges", sourceImage = "controlnet_softedges", apiString = "softedge"),
    LineartAnime(controlNetName = "ControlNet_LineartAnime", sourceImage = "controlnet_lineartanime", apiString = "lineart"),
    NormalMap(controlNetName = "ControlNet_Normal Map", sourceImage = "controlnet_normalmap", apiString = "normal"),
    Dept(controlNetName = "ControlNet_Dept", sourceImage = "controlnet_dept", apiString = "depth"),
    Face(controlNetName = "ControlNet_Face", sourceImage = "controlnet_face", apiString = "face_detector"),
    Scribble(controlNetName = "ControlNet_Scribble", sourceImage = "controlnet_scribble", apiString = "scribble"),
}

enum class SamplingMethod(val display : String ,val apiString: String){
    EulerA(display = "Euler a", apiString = "EulerAncestralDiscreteScheduler"),
    Euler(display = "Euler", apiString = "EulerDiscreteScheduler"),
    LMS(display = "LMS", apiString = "LMSDiscreteScheduler"),
    PNMDM(display = "PNDM", apiString = "PNDMScheduler"),
    DPMSloverPlus(display = "DPMSlover++", apiString = "DPMSolverSinglestepScheduler"),
    HEUNDiscrete(display = "HEUNDiscrete", apiString = "HEUNDiscrete"),
    DDIM(display = "DDIM", apiString = "DDIMScheduler"),
    DDPM(display = "DDPM", apiString = "DDPMScheduler"),
    DDIMI(display = "DDIMI", apiString = "DDIMInverseScheduler"),
    KDPM2A(display = "KDPM2A", apiString = "KDPM2AncestralDiscreteScheduler"),
    DPMSloverMultistep(display = "DPMSloverMultistep", apiString = "DPMSolverMultistepScheduler"),
    DEISMultistep(display = "DEISMultistep", apiString = "DEISMultistepScheduler"),
    UniPCMultistep(display = "UniPCMultistep", apiString = "UniPCMultistepScheduler"),
    IPNDMS(display = "IPNDMS", apiString = "IPNDMScheduler"),
    KarrasVe(display = "KarrasVe", apiString = "KarrasVeScheduler"),
    ScoredeVe(display = "ScoredeVe", apiString = "ScoreSdeVeScheduler"),
}

enum class CharacterAppearance(val id : Int, val display : String){
    Object(id = 1, "Object"),
    Body(id = 2, "Body"),
    HairAndFace(id = 3, "Hair&Face"),
    Clothing(id = 4, "Clothing"),
    View(id = 5, "View"),
    Scence(id = 6, "Scence"),
    Action(id = 7, "Action")
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

