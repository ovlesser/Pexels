package com.ovlesser.pexels.data

val sampleData =
    """
{
    "page": 1,
    "per_page": 2,
    "photos": [
        {
            "id": 6546164,
            "width": 3265,
            "height": 5000,
            "url": "https://www.pexels.com/photo/person-taking-bowl-with-hawaiian-dish-6546164/",
            "photographer": "Larissa Deruzzi",
            "photographer_url": "https://www.pexels.com/@deruzzi",
            "photographer_id": 20198481,
            "avg_color": "#BE695C",
            "src": {
                "original": "https://images.pexels.com/photos/6546164/pexels-photo-6546164.jpeg",
                "large2x": "https://images.pexels.com/photos/6546164/pexels-photo-6546164.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940",
                "large": "https://images.pexels.com/photos/6546164/pexels-photo-6546164.jpeg?auto=compress&cs=tinysrgb&h=650&w=940",
                "medium": "https://images.pexels.com/photos/6546164/pexels-photo-6546164.jpeg?auto=compress&cs=tinysrgb&h=350",
                "small": "https://images.pexels.com/photos/6546164/pexels-photo-6546164.jpeg?auto=compress&cs=tinysrgb&h=130",
                "portrait": "https://images.pexels.com/photos/6546164/pexels-photo-6546164.jpeg?auto=compress&cs=tinysrgb&fit=crop&h=1200&w=800",
                "landscape": "https://images.pexels.com/photos/6546164/pexels-photo-6546164.jpeg?auto=compress&cs=tinysrgb&fit=crop&h=627&w=1200",
                "tiny": "https://images.pexels.com/photos/6546164/pexels-photo-6546164.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"
            },
            "liked": false
        },
        {
            "id": 6805855,
            "width": 5126,
            "height": 4101,
            "url": "https://www.pexels.com/photo/group-of-tourists-walking-on-snowy-hilly-terrain-6805855/",
            "photographer": "Harry Cooke",
            "photographer_url": "https://www.pexels.com/@harry-cooke",
            "photographer_id": 3933683,
            "avg_color": "#738080",
            "src": {
                "original": "https://images.pexels.com/photos/6805855/pexels-photo-6805855.jpeg",
                "large2x": "https://images.pexels.com/photos/6805855/pexels-photo-6805855.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940",
                "large": "https://images.pexels.com/photos/6805855/pexels-photo-6805855.jpeg?auto=compress&cs=tinysrgb&h=650&w=940",
                "medium": "https://images.pexels.com/photos/6805855/pexels-photo-6805855.jpeg?auto=compress&cs=tinysrgb&h=350",
                "small": "https://images.pexels.com/photos/6805855/pexels-photo-6805855.jpeg?auto=compress&cs=tinysrgb&h=130",
                "portrait": "https://images.pexels.com/photos/6805855/pexels-photo-6805855.jpeg?auto=compress&cs=tinysrgb&fit=crop&h=1200&w=800",
                "landscape": "https://images.pexels.com/photos/6805855/pexels-photo-6805855.jpeg?auto=compress&cs=tinysrgb&fit=crop&h=627&w=1200",
                "tiny": "https://images.pexels.com/photos/6805855/pexels-photo-6805855.jpeg?auto=compress&cs=tinysrgb&dpr=1&fit=crop&h=200&w=280"
            },
            "liked": false
        }
    ],
    "total_results": 8000,
    "next_page": "https://api.pexels.com/v1/search/?page=2&per_page=2&query=people"
}
    """.trimIndent()