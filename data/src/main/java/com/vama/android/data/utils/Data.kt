package com.vama.android.data.utils

import com.vama.android.data.model.User

// Internal makes the constant visible only in the module
internal val Dummy_Users: List<User> = listOf(
    User(
        id = 1, name = "Caroline",
        avatarUrl = "https://api.dicebear.com/9.x/miniavs/png",
        address = "Saint-Pierre-du-Mont ; 5km",
        phoneNumber = "+33 6 86 57 90 14",
        aboutMe = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Pellentesque porttitor id sem ut blandit. Lorem ipsum dolor sit amet, " +
                "consectetur adipiscing elit. Donec sed hendrerit ex. Cras a tempus risus. " +
                "Aliquam egestas nulla non luctus blandit. Aenean dignissim massa ultrices volutpat bibendum. " +
                "Integer semper diam et lorem iaculis pulvinar.",
        favorite = true,
        webSite = "www.facebook.fr/caroline"
    ),
    User(
        id = 2,
        name = "Jack",
        avatarUrl = "https://api.dicebear.com/9.x/miniavs/png",
        address = "Saint-Pierre-du-Mont ; 5km",
        phoneNumber = "+33 6 00 55 90 14",
        aboutMe = ("Sed eget fringilla mauris, ac rutrum mauris. Curabitur finibus felis id justo porttitor, " +
                "vitae hendrerit justo imperdiet. Donec tempus quam vulputate, elementum arcu a, molestie felis. " +
                "Pellentesque eu risus luctus, tincidunt odio at, volutpat magna. Nam scelerisque vitae est vitae fermentum. " +
                "Cras suscipit pretium ex, ut condimentum lorem sagittis sit amet. Praesent et gravida diam, at commodo lorem. " +
                "Praesent tortor dui, fermentum vitae sollicitudin ut, elementum ut magna. Nulla volutpat tincidunt lectus, vel malesuada " +
                "ante ultrices id.\n\n" +
                "Ut scelerisque fringilla leo vitae dictum. Nunc suscipit urna tellus, a elementum eros accumsan vitae. " +
                "Nunc lacinia turpis eu consectetur elementum. Cras scelerisque laoreet mauris ac pretium. Nam pellentesque " +
                "ut orci ut scelerisque. Aliquam quis metus egestas, viverra neque vel, ornare velit. Nullam lobortis justo et ipsum " +
                "sodales sodales. Etiam volutpat laoreet tellus, ultrices maximus nulla luctus ultricies. Praesent a dapibus arcu. In at magna " +
                "in velit placerat vehicula nec in purus."),
        favorite = true,
        webSite = "www.facebook.fr/jack"
    ),
    User(
        id = 3,
        name = "Chloé",
        avatarUrl = "https://api.dicebear.com/9.x/miniavs/png",
        address = "Saint-Pierre-du-Mont ; 6km",
        phoneNumber = "+33 6 86 13 12 14",
        aboutMe = "Sed ultricies suscipit semper. Fusce non blandit quam. ",
        favorite = false,
        webSite = "www.facebook.fr/chloe"
    ),
    User(
        id = 4,
        name = "Vincent",
        avatarUrl = "https://api.dicebear.com/9.x/miniavs/png",
        address = "Saint-Pierre-du-Mont ; 11km",
        phoneNumber = "+33 6 10 57 90 19",
        aboutMe = ("Etiam quis neque egestas, consectetur est quis, laoreet augue. " +
                "Interdum et malesuada fames ac ante ipsum primis in faucibus. Morbi ipsum sem, " +
                "commodo in nisi maximus, semper dignissim metus. Fusce eget nunc sollicitudin, dignissim " +
                "tortor quis, consectetur mauris. "),
        favorite = true,
        webSite = "www.facebook.fr/vincent"
    ),
    User(
        id = 5,
        name = "Elodie",
        avatarUrl = "https://api.dicebear.com/9.x/miniavs/png",
        address = "Saint-Pierre-du-Mont ; 8km",
        phoneNumber = "+33 6 86 57 90 14",
        aboutMe = ("Fusce in ligula mi. Aliquam in sagittis tellus. Suspendisse tempor, velit et " +
                "cursus facilisis, eros sapien sollicitudin mauris, et ultrices " +
                "lectus sapien in neque. "),
        favorite = true,
        webSite = "www.facebook.fr/elodie"
    ),
    User(
        id = 6,
        name = "Sylvain",
        avatarUrl = "https://api.dicebear.com/9.x/miniavs/png",
        address = "Saint-Pierre-du-Mont ; 6km",
        phoneNumber = "+33 6 86 12 22 02",
        aboutMe = ("Integer pulvinar lacinia augue, a tempor ex semper eget. Nam lacinia " +
                "lorem dapibus, pharetra ante in, auctor urna. Praesent sollicitudin metus sit " +
                "amet nunc lobortis sodales. In eget ante congue, vestibulum leo ac, placerat leo. " +
                "Nullam arcu purus, cursus a sollicitudin eu, lobortis vitae est. Sed sodales sit " +
                "amet magna nec finibus. Nulla pellentesque, justo ut bibendum mollis, urna diam " +
                "hendrerit dolor, non gravida urna quam id eros. "),
        favorite = false,
        webSite = "www.facebook.fr/sylvain"
    ),
    User(
        id = 7,
        name = "Laetitia",
        avatarUrl = "https://api.dicebear.com/9.x/miniavs/png",
        address = "Saint-Pierre-du-Mont ; 14km",
        phoneNumber = "+33 6 96 57 90 01",
        aboutMe = "Vestibulum non leo vel mi ultrices pellentesque. ",
        favorite = true,
        webSite = "www.facebook.fr/laetitia"
    ),
    User(
        id = 8,
        name = "Dan",
        avatarUrl = "https://api.dicebear.com/9.x/miniavs/png",
        address = "Saint-Pierre-du-Mont ; 1km",
        phoneNumber = "+33 6 86 57 90 14",
        aboutMe = "Cras non dapibus arcu. ",
        favorite = true,
        webSite = "www.facebook.fr/dan"
    ),
    User(
        id = 9,
        name = "Joseph",
        avatarUrl = "https://api.dicebear.com/9.x/miniavs/png",
        address = "Saint-Pierre-du-Mont ; 2km",
        phoneNumber = "+33 6 86 57 90 14",
        aboutMe = ("Donec neque odio, eleifend a luctus ac, tempor non libero. " +
                "Nunc ullamcorper, erat non viverra feugiat, massa odio facilisis ligula, " +
                "ut pharetra risus libero eget elit. Nulla malesuada, purus eu malesuada malesuada, est " +
                "purus ullamcorper ipsum, quis congue velit mi sed lorem."),
        favorite = false,
        webSite = "www.facebook.fr/joseph"
    ),
    User(
        id = 10,
        name = "Emma",
        avatarUrl = "https://api.dicebear.com/9.x/miniavs/png",
        address = "Saint-Pierre-du-Mont ; 1km",
        phoneNumber = "+33 6 14 71 70 18",
        aboutMe = ("Aliquam erat volutpat. Mauris at massa nibh. Suspendisse auctor fermentum orci, " +
                "nec auctor tortor. Ut eleifend euismod turpis vitae tempus. Sed a tincidunt ex. Etiam ut " +
                "nibh ante. Fusce maximus lorem nibh, at sollicitudin ante ultrices vel. Praesent ac luctus ante, eu " +
                "lacinia diam. Quisque malesuada vel arcu vitae euismod. Ut egestas mattis euismod.\n\n" +
                "Vestibulum non leo vel mi ultrices pellentesque. Praesent ornare ligula " +
                "non elit consectetur, pellentesque fringilla eros placerat. Etiam consequat dui " +
                "eleifend justo iaculis, ac ullamcorper velit consectetur. Aliquam vitae elit ac ante ultricies aliquet " +
                "vel at felis. Suspendisse ac placerat odio. Cras non dapibus arcu. Fusce pharetra nisi at orci rhoncus, " +
                "vitae tristique nibh euismod."),
        favorite = false,
        webSite = "www.facebook.fr/emma"
    ),
    User(
        id = 11,
        name = "Patrick",
        avatarUrl = "https://api.dicebear.com/9.x/miniavs/png",
        address = "Saint-Pierre-du-Mont ; 5km",
        phoneNumber = "+33 6 20 40 60 80",
        aboutMe = ("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce porttitor molestie " +
                "libero, nec porta lectus posuere a. Curabitur in purus at enim commodo accumsan. Nullam molestie diam a massa finibus, in bibendum " +
                "nulla ullamcorper. Nunc at enim enim. Maecenas quis posuere nisi. Mauris suscipit cursus velit id condimentum. Ut finibus turpis " +
                "at nulla finibus sollicitudin. Nunc dolor mauris, blandit id ullamcorper vel, lacinia ac nisi. Vestibulum et sapien tempor, " +
                "egestas lorem vitae, faucibus urna. Duis id odio massa. Class aptent taciti sociosqu ad litora torquent per conubia nostra, " +
                "per inceptos himenaeos.\n\nLorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce porttitor molestie  " +
                "libero, nec porta lectus posuere a. Curabitur in purus at enim commodo accumsan. Nullam molestie diam a massa finibus, in bibendum " +
                "nulla ullamcorper. Nunc at enim enim. Maecenas quis posuere nisi. Mauris suscipit cursus velit id condimentum. Ut finibus turpis " +
                "at nulla finibus sollicitudin. Nunc dolor mauris, blandit id ullamcorper vel, lacinia ac nisi. Vestibulum et sapien tempor, " +
                "egestas lorem vitae, faucibus urna. Duis id odio massa. Class aptent taciti sociosqu ad litora torquent per conubia nostra, " +
                "per inceptos himenaeos."),
        favorite = true,
        webSite = "www.facebook.fr/patrick"
    ),
    User(
        id = 12,
        name = "Ludovic",
        avatarUrl = "https://api.dicebear.com/9.x/miniavs/png",
        address = "Saint-Pierre-du-Mont ; 5km",
        phoneNumber = "+33 6 00 01 10 11",
        aboutMe = ("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce porttitor molestie " +
                "libero, nec porta lectus posuere a. Curabitur in purus at enim commodo accumsan. Nullam molestie diam a massa finibus, in bibendum " +
                "nulla ullamcorper. Nunc at enim enim. Maecenas quis posuere nisi. Mauris suscipit cursus velit id condimentum. Ut finibus turpis " +
                "at nulla finibus sollicitudin. Nunc dolor mauris, blandit id ullamcorper vel, lacinia ac nisi. Vestibulum et sapien tempor, " +
                "egestas lorem vitae, faucibus urna. Duis id odio massa. Class aptent taciti sociosqu ad litora torquent per conubia nostra, " +
                "per inceptos himenaeos.\n\nLorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce porttitor molestie  " +
                "libero, nec porta lectus posuere a. Curabitur in purus at enim commodo accumsan. Nullam molestie diam a massa finibus, in bibendum " +
                "nulla ullamcorper. Nunc at enim enim. Maecenas quis posuere nisi. Mauris suscipit cursus velit id condimentum. Ut finibus turpis " +
                "at nulla finibus sollicitudin. Nunc dolor mauris, blandit id ullamcorper vel, lacinia ac nisi. Vestibulum et sapien tempor, " +
                "egestas lorem vitae, faucibus urna. Duis id odio massa. Class aptent taciti sociosqu ad litora torquent per conubia nostra, " +
                "per inceptos himenaeos."),
        favorite = false,
        webSite = "www.facebook.fr/ludovic"
    )
)

internal val Database_Users: List<User> = listOf(
    User(
        id = 1, name = "Carole",
        avatarUrl = "https://api.dicebear.com/9.x/miniavs/png",
        address = "Saint-Pierre-du-Mont ; 5km",
        phoneNumber = "+33 6 86 57 90 14",
        aboutMe = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Pellentesque porttitor id sem ut blandit. Lorem ipsum dolor sit amet, " +
                "consectetur adipiscing elit. Donec sed hendrerit ex. Cras a tempus risus. " +
                "Aliquam egestas nulla non luctus blandit. Aenean dignissim massa ultrices volutpat bibendum. " +
                "Integer semper diam et lorem iaculis pulvinar.",
        favorite = true,
        webSite = "www.facebook.fr/caroline"
    ),
    User(
        id = 2,
        name = "Jack",
        avatarUrl = "https://api.dicebear.com/9.x/miniavs/png",
        address = "Saint-Pierre-du-Mont ; 5km",
        phoneNumber = "+33 6 00 55 90 14",
        aboutMe = ("Sed eget fringilla mauris, ac rutrum mauris. Curabitur finibus felis id justo porttitor, " +
                "vitae hendrerit justo imperdiet. Donec tempus quam vulputate, elementum arcu a, molestie felis. " +
                "Pellentesque eu risus luctus, tincidunt odio at, volutpat magna. Nam scelerisque vitae est vitae fermentum. " +
                "Cras suscipit pretium ex, ut condimentum lorem sagittis sit amet. Praesent et gravida diam, at commodo lorem. " +
                "Praesent tortor dui, fermentum vitae sollicitudin ut, elementum ut magna. Nulla volutpat tincidunt lectus, vel malesuada " +
                "ante ultrices id.\n\n" +
                "Ut scelerisque fringilla leo vitae dictum. Nunc suscipit urna tellus, a elementum eros accumsan vitae. " +
                "Nunc lacinia turpis eu consectetur elementum. Cras scelerisque laoreet mauris ac pretium. Nam pellentesque " +
                "ut orci ut scelerisque. Aliquam quis metus egestas, viverra neque vel, ornare velit. Nullam lobortis justo et ipsum " +
                "sodales sodales. Etiam volutpat laoreet tellus, ultrices maximus nulla luctus ultricies. Praesent a dapibus arcu. In at magna " +
                "in velit placerat vehicula nec in purus."),
        favorite = true,
        webSite = "www.facebook.fr/jack"
    ),
    User(
        id = 3,
        name = "Chloé",
        avatarUrl = "https://api.dicebear.com/9.x/miniavs/png",
        address = "Saint-Pierre-du-Mont ; 6km",
        phoneNumber = "+33 6 86 13 12 14",
        aboutMe = "Sed ultricies suscipit semper. Fusce non blandit quam. ",
        favorite = false,
        webSite = "www.facebook.fr/chloe"
    ),
    User(
        id = 4,
        name = "Vincent",
        avatarUrl = "https://api.dicebear.com/9.x/miniavs/png",
        address = "Saint-Pierre-du-Mont ; 11km",
        phoneNumber = "+33 6 10 57 90 19",
        aboutMe = ("Etiam quis neque egestas, consectetur est quis, laoreet augue. " +
                "Interdum et malesuada fames ac ante ipsum primis in faucibus. Morbi ipsum sem, " +
                "commodo in nisi maximus, semper dignissim metus. Fusce eget nunc sollicitudin, dignissim " +
                "tortor quis, consectetur mauris. "),
        favorite = true,
        webSite = "www.facebook.fr/vincent"
    ),
    User(
        id = 5,
        name = "Elodie",
        avatarUrl = "https://api.dicebear.com/9.x/miniavs/png",
        address = "Saint-Pierre-du-Mont ; 8km",
        phoneNumber = "+33 6 86 57 90 14",
        aboutMe = ("Fusce in ligula mi. Aliquam in sagittis tellus. Suspendisse tempor, velit et " +
                "cursus facilisis, eros sapien sollicitudin mauris, et ultrices " +
                "lectus sapien in neque. "),
        favorite = true,
        webSite = "www.facebook.fr/elodie"
    ),
    User(
        id = 6,
        name = "Sylvain",
        avatarUrl = "https://api.dicebear.com/9.x/miniavs/png",
        address = "Saint-Pierre-du-Mont ; 6km",
        phoneNumber = "+33 6 86 12 22 02",
        aboutMe = ("Integer pulvinar lacinia augue, a tempor ex semper eget. Nam lacinia " +
                "lorem dapibus, pharetra ante in, auctor urna. Praesent sollicitudin metus sit " +
                "amet nunc lobortis sodales. In eget ante congue, vestibulum leo ac, placerat leo. " +
                "Nullam arcu purus, cursus a sollicitudin eu, lobortis vitae est. Sed sodales sit " +
                "amet magna nec finibus. Nulla pellentesque, justo ut bibendum mollis, urna diam " +
                "hendrerit dolor, non gravida urna quam id eros. "),
        favorite = false,
        webSite = "www.facebook.fr/sylvain"
    ),
    User(
        id = 7,
        name = "Laetitia",
        avatarUrl = "https://api.dicebear.com/9.x/miniavs/png",
        address = "Saint-Pierre-du-Mont ; 14km",
        phoneNumber = "+33 6 96 57 90 01",
        aboutMe = "Vestibulum non leo vel mi ultrices pellentesque. ",
        favorite = true,
        webSite = "www.facebook.fr/laetitia"
    ),
    User(
        id = 8,
        name = "Dan",
        avatarUrl = "https://api.dicebear.com/9.x/miniavs/png",
        address = "Saint-Pierre-du-Mont ; 1km",
        phoneNumber = "+33 6 86 57 90 14",
        aboutMe = "Cras non dapibus arcu. ",
        favorite = true,
        webSite = "www.facebook.fr/dan"
    ),
    User(
        id = 9,
        name = "Joseph",
        avatarUrl = "https://api.dicebear.com/9.x/miniavs/png",
        address = "Saint-Pierre-du-Mont ; 2km",
        phoneNumber = "+33 6 86 57 90 14",
        aboutMe = ("Donec neque odio, eleifend a luctus ac, tempor non libero. " +
                "Nunc ullamcorper, erat non viverra feugiat, massa odio facilisis ligula, " +
                "ut pharetra risus libero eget elit. Nulla malesuada, purus eu malesuada malesuada, est " +
                "purus ullamcorper ipsum, quis congue velit mi sed lorem."),
        favorite = false,
        webSite = "www.facebook.fr/joseph"
    ),
    User(
        id = 10,
        name = "Emma",
        avatarUrl = "https://api.dicebear.com/9.x/miniavs/png",
        address = "Saint-Pierre-du-Mont ; 1km",
        phoneNumber = "+33 6 14 71 70 18",
        aboutMe = ("Aliquam erat volutpat. Mauris at massa nibh. Suspendisse auctor fermentum orci, " +
                "nec auctor tortor. Ut eleifend euismod turpis vitae tempus. Sed a tincidunt ex. Etiam ut " +
                "nibh ante. Fusce maximus lorem nibh, at sollicitudin ante ultrices vel. Praesent ac luctus ante, eu " +
                "lacinia diam. Quisque malesuada vel arcu vitae euismod. Ut egestas mattis euismod.\n\n" +
                "Vestibulum non leo vel mi ultrices pellentesque. Praesent ornare ligula " +
                "non elit consectetur, pellentesque fringilla eros placerat. Etiam consequat dui " +
                "eleifend justo iaculis, ac ullamcorper velit consectetur. Aliquam vitae elit ac ante ultricies aliquet " +
                "vel at felis. Suspendisse ac placerat odio. Cras non dapibus arcu. Fusce pharetra nisi at orci rhoncus, " +
                "vitae tristique nibh euismod."),
        favorite = false,
        webSite = "www.facebook.fr/emma"
    ),
    User(
        id = 11,
        name = "Patrick",
        avatarUrl = "https://api.dicebear.com/9.x/miniavs/png",
        address = "Saint-Pierre-du-Mont ; 5km",
        phoneNumber = "+33 6 20 40 60 80",
        aboutMe = ("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce porttitor molestie " +
                "libero, nec porta lectus posuere a. Curabitur in purus at enim commodo accumsan. Nullam molestie diam a massa finibus, in bibendum " +
                "nulla ullamcorper. Nunc at enim enim. Maecenas quis posuere nisi. Mauris suscipit cursus velit id condimentum. Ut finibus turpis " +
                "at nulla finibus sollicitudin. Nunc dolor mauris, blandit id ullamcorper vel, lacinia ac nisi. Vestibulum et sapien tempor, " +
                "egestas lorem vitae, faucibus urna. Duis id odio massa. Class aptent taciti sociosqu ad litora torquent per conubia nostra, " +
                "per inceptos himenaeos.\n\nLorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce porttitor molestie  " +
                "libero, nec porta lectus posuere a. Curabitur in purus at enim commodo accumsan. Nullam molestie diam a massa finibus, in bibendum " +
                "nulla ullamcorper. Nunc at enim enim. Maecenas quis posuere nisi. Mauris suscipit cursus velit id condimentum. Ut finibus turpis " +
                "at nulla finibus sollicitudin. Nunc dolor mauris, blandit id ullamcorper vel, lacinia ac nisi. Vestibulum et sapien tempor, " +
                "egestas lorem vitae, faucibus urna. Duis id odio massa. Class aptent taciti sociosqu ad litora torquent per conubia nostra, " +
                "per inceptos himenaeos."),
        favorite = true,
        webSite = "www.facebook.fr/patrick"
    ),
    User(
        id = 12,
        name = "Ludovic",
        avatarUrl = "https://api.dicebear.com/9.x/miniavs/png",
        address = "Saint-Pierre-du-Mont ; 5km",
        phoneNumber = "+33 6 00 01 10 11",
        aboutMe = ("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce porttitor molestie " +
                "libero, nec porta lectus posuere a. Curabitur in purus at enim commodo accumsan. Nullam molestie diam a massa finibus, in bibendum " +
                "nulla ullamcorper. Nunc at enim enim. Maecenas quis posuere nisi. Mauris suscipit cursus velit id condimentum. Ut finibus turpis " +
                "at nulla finibus sollicitudin. Nunc dolor mauris, blandit id ullamcorper vel, lacinia ac nisi. Vestibulum et sapien tempor, " +
                "egestas lorem vitae, faucibus urna. Duis id odio massa. Class aptent taciti sociosqu ad litora torquent per conubia nostra, " +
                "per inceptos himenaeos.\n\nLorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce porttitor molestie  " +
                "libero, nec porta lectus posuere a. Curabitur in purus at enim commodo accumsan. Nullam molestie diam a massa finibus, in bibendum " +
                "nulla ullamcorper. Nunc at enim enim. Maecenas quis posuere nisi. Mauris suscipit cursus velit id condimentum. Ut finibus turpis " +
                "at nulla finibus sollicitudin. Nunc dolor mauris, blandit id ullamcorper vel, lacinia ac nisi. Vestibulum et sapien tempor, " +
                "egestas lorem vitae, faucibus urna. Duis id odio massa. Class aptent taciti sociosqu ad litora torquent per conubia nostra, " +
                "per inceptos himenaeos."),
        favorite = false,
        webSite = "www.facebook.fr/ludovic"
    )
)

