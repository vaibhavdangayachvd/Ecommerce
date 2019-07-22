-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Jul 22, 2019 at 01:19 AM
-- Server version: 10.3.16-MariaDB
-- PHP Version: 7.3.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `id10188401_ecommerce`
--

-- --------------------------------------------------------

--
-- Table structure for table `addresses`
--

CREATE TABLE `addresses` (
  `id` int(11) NOT NULL,
  `address` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `username` varchar(30) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `cart`
--

CREATE TABLE `cart` (
  `username` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `productId` int(11) NOT NULL,
  `quantity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `cart`
--

INSERT INTO `cart` (`username`, `productId`, `quantity`) VALUES
('vaibhavdangayachvd', 9, 1);

-- --------------------------------------------------------

--
-- Table structure for table `categories`
--

CREATE TABLE `categories` (
  `name` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `image` varchar(30) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `categories`
--

INSERT INTO `categories` (`name`, `image`) VALUES
('clothing', 'clothing.jpg'),
('furniture', 'furniture.jpg'),
('mobile', 'mobile.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `id` int(11) NOT NULL,
  `name` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `image` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `price` int(11) NOT NULL,
  `description` text COLLATE utf8_unicode_ci NOT NULL,
  `category` varchar(30) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`id`, `name`, `image`, `price`, `description`, `category`) VALUES
(1, 'pocoF1byXiaomi', 'pocoF1.jpeg', 28999, '8 GB RAM | 256 GB ROM | Expandable Upto 256 GB\r\n15.7 cm (6.18 inch) FHD+ Display\r\n12MP + 5MP | 20MP Front Camera\r\n4000 mAh Li-polymer Battery\r\nQualcomm Snapdragon 845 Processor', 'mobile'),
(2, 'realme3', 'realme3.jpeg', 8999, '3 GB RAM | 32 GB ROM | Expandable Upto 256 GB\r\n15.8 cm (6.22 inch) HD+ Display\r\n13MP + 2MP | 13MP Front Camera\r\n4230 mAh Battery\r\nMediaTek Helio P70 Octa Core 2.1 GHz AI Processor\r\nTriple Slots (Dual SIM + Memory Card)\r\nFingerprint Sensor and Face Unlock', 'mobile'),
(3, 'samsungGalaxym30', 'samsungGalaxym30.jpeg', 16490, '4 GB RAM | 64 GB ROM |\r\n16.26 cm (6.4 inch) Display\r\n13MP Rear Camera\r\n5000 mAh Battery', 'mobile'),
(4, 'oppoA5', 'oppoA5.jpeg', 11990, '4 GB RAM | 32 GB ROM | Expandable Upto 256 GB\r\n15.75 cm (6.2 inch) HD+ Display\r\n13MP + 2MP | 8MP Front Camera\r\n4230 mAh Li-ion Battery\r\nQualcomm Snapdragon 450 Processor', 'mobile'),
(5, 'appleIphone8plus', 'appleIphone8plus.jpeg', 61499, '64 GB ROM |\r\n13.97 cm (5.5 inch) Retina HD Display\r\n12MP + 12MP | 7MP Front Camera\r\nA11 Bionic Chip with 64-bit Architecture, Neural Engine, Embedded M11 Motion Coprocessor Processor', 'mobile'),
(6, 'asusZenfonec', 'asusZenfonec.jpeg', 5988, '1 GB RAM | 8 GB ROM | Expandable Upto 64 GB\r\n11.43 cm (4.5 inch) FWVGA Display\r\n5MP Rear Camera | 0.3MP Front Camera\r\n2100 mAh Battery\r\nIntel Atom Z2520 Processor', 'mobile'),
(7, ' honor20', 'honor20.jpeg', 32999, '6 GB RAM | 128 GB ROM |\r\n15.9 cm (6.26 inch) Display\r\n48MP + 16MP + 2MP + 2MP | 32MP Front Camera\r\n3750 mAh Battery\r\nHiSilicon Kirin 980 Processor', 'mobile'),
(8, 'solidMenpoloNeck', 'solidMenpoloNeck.jpeg', 452, 'Type Polo Neck\r\nSleeve Full Sleeve\r\nFit Regular\r\nFabric Cotton Blend\r\nSales Package Pack of 1 Tshirt\r\nPack of 1\r\nStyle Code Style Tshirt\r\nNeck Type Polo Neck', 'clothing'),
(9, 'regularMenblueJeans', 'regularMenblueJeans.jpeg', 7000, 'Style Code smO0406\r\nIdeal For Men\r\nSuitable For Western Wear\r\nPack Of 1\r\nPattern Washed\r\nReversible No\r\nFabric Cotton Blend', 'clothing'),
(10, 'alcovePolkaprintTie', 'alcovePolkaprintTie.jpeg', 125, 'Type: Neck Tie\r\nFabric: Cotton Satin Blend\r\nPack of: 1\r\nColor: Blue\r\nPattern: Polka Print', 'clothing'),
(11, 'menSolidmidCrew', 'menSolidmidCrew.jpeg', 59, 'Color Blue\r\nPattern Solid\r\nIdeal For Men\r\nFabric Cotton Blend', 'clothing'),
(12, 'embroideredFentamsilkAnarkaligown', 'embroideredFentamsilkAnarkaligown.jpeg', 1104, 'Style Code Chand\r\nNeck Round Neck\r\nSleeve Half Sleeve\r\nFabric Care Dry Clean Only\r\nFabric Fentam Silk\r\nType Anarkali\r\nStitching Type Semi Stitched\r\nSales Package 1 Dupptta, 1 Top', 'clothing'),
(13, 'floralPrintbollywoodPurechiffon', 'floralPrintbollywoodPurechiffon.jpeg', 449, 'Style Code 512S832A\r\nPattern Floral Print\r\nPack of 1\r\nSecondary Color Brown, Pink\r\nOccasion Casual\r\nType of Embroidery None\r\nHand Embroidery No\r\nDecorative Material Lace', 'clothing'),
(14, 'womenGownwhiteDress', 'womenGownwhiteDress.jpeg', 2699, 'Color White\r\nLength Maxi/Full Length\r\nFabric Polycotton\r\nIdeal For Women\r\nType Gown\r\nStyle Code TPRSS18UZ0023\r\nSuitable For Western Wear', 'clothing'),
(15, 'silkCottonblendWovenblackWomendupatta', 'silkCottonblendWovenblackWomendupatta.jpeg', 899, 'Style Code DUP06_BLACK\r\nOccasion Party & Festive\r\nWeave Type Banarasi\r\nRegion Banarasi\r\nFabric Care Dry Clean\r\nOther Details Banarasi Dupatta\r\nFabric Cotton Blend, Polycotton, Raw Silk, Silk Cotton Blend\r\nPattern Woven', 'clothing'),
(16, 'perfectHomesrhapsodyEngineeredwoodQueenboxBed', 'perfectHomesrhapsodyEngineeredwoodQueenboxBed.jpeg', 12690, 'Material Subtype: Particle Board\r\nW x H x D: 162.3 cm x 89 cm x 218 cm (5 ft 3 in x 2 ft 11 in x 7 ft 1 in)\r\nKnock Down - Delivered in non-assembled pieces, installation by service partner', 'furniture'),
(17, 'mueblesCasamarcoLeatherette6seaterSofa', 'mueblesCasamarcoLeatherette6seaterSofa.jpeg', 30390, 'Leatherette\r\nLeft Facing\r\nFilling Material: Foam\r\nW x H x D: 259.08 cm x 83.82 cm x 226.06 cm (8 ft 6 in x 2 ft 9 in x 7 ft 5 in)\r\nPre-assembled', 'furniture'),
(18, 'crystalFurnitechsigmaEngineeredwoodCoffeetable', 'crystalFurnitechsigmaEngineeredwoodCoffeetable.jpeg', 2990, 'Particle Board\r\nEngineered Wood\r\nW x H x D: 118 cm x 38 cm x 60 cm (3 ft 10 in x 1 ft 2 in x 1 ft 11 in)\r\nDelivery Condition: Knock Down', 'furniture'),
(19, 'godrejInterioslidenStoreproPlusmetalAlmirah', 'godrejInterioslidenStoreproPlusmetalAlmirah.jpeg', 39730, 'Type: Almirah\r\nContemporary & Modern Style\r\nSuitable For: Bedroom\r\nKnock Down\r\nW x H x D: 1350 mm x 2400 mm x 600 mm (4 ft 5 in x 7 ft 10 in x 1 ft 11 in)', 'furniture'),
(20, 'perfectHomeswebsterTvEntertainmentunit', 'perfectHomeswebsterTvEntertainmentunit.jpeg', 11990, 'Unit Material Subtype: Particle Board\r\nW x H x D: 200 cm x 170 cm x 37.08 cm (6 ft 6 in x 5 ft 6 in x 1 ft 2 in)\r\nIdeal TV Size: 55 inch\r\nNumber of Drawers: 1, Number of Open Shelves: 8, Number of Closed Shelves: 4\r\nKnock Down - Delivered in non-assembled pieces, installation by service partner', 'furniture'),
(21, 'stylespaQuadroengineeredWoodfreeStandingchestOfdrawers', 'stylespaQuadroengineeredWoodfreeStandingchestOfdrawers.jpeg', 7490, 'Type: Chest of Drawers | Material: Engineered Wood\r\n5 Shelves\r\n2 Drawers\r\nWidth x Height: 113.9 cm x 76.8 cm (3 ft 8 in x 2 ft 6 in)\r\nKnock Down', 'furniture'),
(22, 'cirrusBypepsComfortplush5InchqueenCoirmattress', 'cirrusBypepsComfortplush5InchqueenCoirmattress.jpeg', 13090, 'Length: 75 inch, Width: 60 inch, Thickness: 5 inch (6 ft 3 in x 5 ft x 5 in)\r\nSupport Type: Coir\r\nComfort Layer: PU Foam\r\nMattress Features: Sag Resistant Mattress\r\nFrom the House of Peps', 'furniture'),
(23, 'nexisSundryxxlTeardrop', 'nexisSundryxxlTeardrop.jpeg', 729, 'Brand Nexis Sundry\r\nModel Number High Quality Bean Bag Cover Retro Classic (Without Filling) XXL BLACK & BROWN\r\nSize XXL\r\nBrand Color Multicolor\r\nType Tear Drop\r\nColor Black, Brown', 'furniture');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `firstName` varchar(20) NOT NULL,
  `lastName` varchar(20) NOT NULL,
  `username` varchar(30) NOT NULL,
  `password` varchar(40) NOT NULL,
  `email` varchar(30) NOT NULL,
  `gender` tinyint(1) NOT NULL,
  `mobile` varchar(10) NOT NULL,
  `hasDp` tinyint(4) NOT NULL DEFAULT 0
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`firstName`, `lastName`, `username`, `password`, `email`, `gender`, `mobile`, `hasDp`) VALUES
('Vaibhav', 'Dangayach', 'vaibhavdangayachvd', 'af05effa72407dcad1c937acf6eae39260384987', 'vaibhavdangayachvd@gmail.com', 1, '7877480410', 1),
('Shantanu', 'Unknown', 'shantanu', '7288edd0fc3ffcbe93a0cf06e3568e28521687bc', 'shantanu@unknown.com', 1, '9876543210', 1),
('Harsh', 'Dangayach', 'harshdangayachhd', 'cffb36a1af2ceb1658d11904a4e8f6d7d9348a84', 'harshdangayachhd@gmail.com', 1, '7424965480', 1),
('Muskan', 'Agrawal', 'muskan15', 'ffc48769e0927b149624c1971a8a954d77563a88', 'muskanagrawal2012@gmail.com', 2, '9636148374', 1),
('Santanu', 'Banerjee', 'santanu', '40bd001563085fc35165329ea1ff5c5ecbdbbeef', 'santa.banerjee@gmail.com', 1, '7278284618', 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `addresses`
--
ALTER TABLE `addresses`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `categories`
--
ALTER TABLE `categories`
  ADD PRIMARY KEY (`name`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`username`),
  ADD UNIQUE KEY `email` (`email`,`mobile`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `addresses`
--
ALTER TABLE `addresses`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
