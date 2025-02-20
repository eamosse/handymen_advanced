package com.vama.android.data.utils

import com.vama.android.data.model.User

// Internal makes the constant visible only in the module
internal val Dummy_Users: List<User> = listOf(
    User(
        id = 1, name = "Caroline",
        avatarUrl = "https://i.picsum.photos/id/1011/5472/3648.jpg?hmac=Koo9845x2akkVzVFX3xxAc9BCkeGYA9VRVfLE4f0Zzk",
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
        avatarUrl = "https://i.picsum.photos/id/1012/3973/2639.jpg?hmac=s2eybz51lnKy2ZHkE2wsgc6S81fVD1W2NKYOSh8bzDc",
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
        avatarUrl = "https://i.picsum.photos/id/1027/2848/4272.jpg?hmac=EAR-f6uEqI1iZJjB6-NzoZTnmaX0oI0th3z8Y78UpKM",
        address = "Saint-Pierre-du-Mont ; 6km",
        phoneNumber = "+33 6 86 13 12 14",
        aboutMe = "Sed ultricies suscipit semper. Fusce non blandit quam. ",
        favorite = false,
        webSite = "www.facebook.fr/chloe"
    ),
    User(
        id = 4,
        name = "Vincent",
        avatarUrl = "https://i.picsum.photos/id/22/4434/3729.jpg?hmac=fjZdkSMZJNFgsoDh8Qo5zdA_nSGUAWvKLyyqmEt2xs0",
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
        avatarUrl = "https://i.picsum.photos/id/399/2048/1365.jpg?hmac=Tm7jwbWj0i70u952g5yC0da-gxScdY2mQ6gjKrP8Haw",
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
        avatarUrl = "https://i.picsum.photos/id/375/5184/3456.jpg?hmac=3OUWWnSmq1CUXU7cmTnctSvhQYvyME_osftkbJynX04",
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
        avatarUrl = "https://i.picsum.photos/id/628/2509/1673.jpg?hmac=TUdtbj7l4rQx5WGHuFiV_9ArjkAkt6w2Zx8zz-aFwwY",
        address = "Saint-Pierre-du-Mont ; 14km",
        phoneNumber = "+33 6 96 57 90 01",
        aboutMe = "Vestibulum non leo vel mi ultrices pellentesque. ",
        favorite = true,
        webSite = "www.facebook.fr/laetitia"
    ),
    User(
        id = 8,
        name = "Dan",
        avatarUrl = "https://i.picsum.photos/id/453/2048/1365.jpg?hmac=A8uxtdn4Y600Z5b2ngnn9hCXAx8sUnOVzprtDnz6DK8",
        address = "Saint-Pierre-du-Mont ; 1km",
        phoneNumber = "+33 6 86 57 90 14",
        aboutMe = "Cras non dapibus arcu. ",
        favorite = true,
        webSite = "www.facebook.fr/dan"
    ),
    User(
        id = 9,
        name = "Joseph",
        avatarUrl = "https://i.picsum.photos/id/473/5616/3744.jpg?hmac=4tP7GutJ3LGRXeprD581uaNnJJGrhF57f08OOtMm1q0",
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
        avatarUrl = "https://i.picsum.photos/id/64/4326/2884.jpg?hmac=9_SzX666YRpR_fOyYStXpfSiJ_edO3ghlSRnH2w09Kg",
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
        avatarUrl = "https://i.picsum.photos/id/91/3504/2336.jpg?hmac=tK6z7RReLgUlCuf4flDKeg57o6CUAbgklgLsGL0UowU",
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
        avatarUrl = "https://i.picsum.photos/id/804/6000/3376.jpg?hmac=AZ4VZdij0jPu8BKZRbiE2lEMJGGjSFv43ii3RHRugco",
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

