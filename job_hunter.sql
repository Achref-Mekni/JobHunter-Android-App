-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1
-- Généré le :  lun. 30 nov. 2020 à 22:17
-- Version du serveur :  10.1.35-MariaDB
-- Version de PHP :  7.2.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `job_hunter`
--
CREATE DATABASE IF NOT EXISTS `job_hunter` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `job_hunter`;

-- --------------------------------------------------------

--
-- Structure de la table `certification`
--

CREATE TABLE `certification` (
  `id` int(11) NOT NULL,
  `label` varchar(50) DEFAULT NULL,
  `cert_authority` varchar(50) DEFAULT NULL,
  `licence_num` varchar(50) DEFAULT NULL,
  `if_expire` tinyint(1) DEFAULT NULL,
  `cert_date` varchar(20) DEFAULT NULL,
  `expire_date` varchar(20) DEFAULT NULL,
  `cv_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `certification`
--

INSERT INTO `certification` (`id`, `label`, `cert_authority`, `licence_num`, `if_expire`, `cert_date`, `expire_date`, `cv_id`) VALUES
(1, 'TCF B2', 'IFT', '1125665842', 1, '2015-09-10', '2017-09-10', 13),
(2, 'CCNA', 'Cisco', 'AHK5584', 0, '2018-06-15', NULL, 13);

-- --------------------------------------------------------

--
-- Structure de la table `cv`
--

CREATE TABLE `cv` (
  `id` int(11) NOT NULL,
  `creation_date` varchar(50) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `cv`
--

INSERT INTO `cv` (`id`, `creation_date`, `user_id`) VALUES
(10, '04/12/2018', 15),
(11, '10/12/2018', 16),
(12, '10/12/2018', 17),
(13, '10/12/2018', 1),
(17, '04/01/2019', 21);

-- --------------------------------------------------------

--
-- Structure de la table `education`
--

CREATE TABLE `education` (
  `id` int(11) NOT NULL,
  `inst_name` varchar(100) DEFAULT NULL,
  `start_date` varchar(20) DEFAULT NULL,
  `end_planned_date` varchar(20) DEFAULT NULL,
  `degree` varchar(100) DEFAULT NULL,
  `domain` varchar(50) DEFAULT NULL,
  `result` varchar(20) DEFAULT NULL,
  `description` text,
  `cv_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `education`
--

INSERT INTO `education` (`id`, `inst_name`, `start_date`, `end_planned_date`, `degree`, `domain`, `result`, `description`, `cv_id`) VALUES
(1, 'Lycée Mongi Slim', '1/2009', '8/2013', 'Baccalaureat', 'Informatique', '11.68', NULL, 13),
(2, 'Faculté des sciences economique et de gestion de sfax', '1/2013', '7/2016', 'Licence', 'informatique de gestion', '13', NULL, 13),
(10, 'esprit', '12/2019', '7/2020', 'ingineer', 'computer science ', '12.5', 'ggjjfhurfhtr', 13);

-- --------------------------------------------------------

--
-- Structure de la table `internship`
--

CREATE TABLE `internship` (
  `id` int(11) NOT NULL,
  `label` varchar(50) DEFAULT NULL,
  `description` text,
  `start_date` varchar(50) DEFAULT NULL,
  `educ_req` varchar(10) DEFAULT NULL,
  `duration` varchar(50) DEFAULT NULL,
  `skills` varchar(200) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `internship`
--

INSERT INTO `internship` (`id`, `label`, `description`, `start_date`, `educ_req`, `duration`, `skills`, `user_id`) VALUES
(5, 'stage pfe wordpress', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.', '21:43:31%2018/01/2019', 'Bac+3', '3 months', '[php,wordpress,css,javascript,php]', 4),
(7, 'stage 1', 'je', '01:33:30%2002/02/2019', 'bac+5', '[php]', '6 months', 4);

-- --------------------------------------------------------

--
-- Structure de la table `job`
--

CREATE TABLE `job` (
  `id` int(11) NOT NULL,
  `label` varchar(50) DEFAULT NULL,
  `description` text,
  `start_date` varchar(50) DEFAULT NULL,
  `contract_type` varchar(20) DEFAULT NULL,
  `career_req` varchar(20) DEFAULT NULL,
  `skills` varchar(200) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `job`
--

INSERT INTO `job` (`id`, `label`, `description`, `start_date`, `contract_type`, `career_req`, `skills`, `user_id`) VALUES
(8, 'poste developpeur en j2ee', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.', '21:43:31%2018/01/2019', 'Temporary', '5years', '[php,css]', 4),
(9, 'poste developpeur en .net', '.net c# WPF ASP', '21:43:31%2018/01/2019', 'Temporary', '2years', '[php,c#]', 4);

-- --------------------------------------------------------

--
-- Structure de la table `language`
--

CREATE TABLE `language` (
  `id` int(11) NOT NULL,
  `label` varchar(20) DEFAULT NULL,
  `level` varchar(20) DEFAULT NULL,
  `cv_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `language`
--

INSERT INTO `language` (`id`, `label`, `level`, `cv_id`) VALUES
(1, 'Francais', 'B2', 13),
(2, 'Anglais', 'C1', 13);

-- --------------------------------------------------------

--
-- Structure de la table `message`
--

CREATE TABLE `message` (
  `id` int(11) NOT NULL,
  `sender` int(11) DEFAULT NULL,
  `receiver` int(11) DEFAULT NULL,
  `content` text,
  `sent_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `message`
--

INSERT INTO `message` (`id`, `sender`, `receiver`, `content`, `sent_at`) VALUES
(1, 1, 4, 'hey', '2019-01-04 13:30:25'),
(2, 4, 1, 'hi', '2019-01-04 13:30:33'),
(3, 1, 4, 'how r u ?', '2019-01-04 13:31:15'),
(4, 1, 4, 'edded', '2019-01-04 13:31:27'),
(5, 1, 4, 'how are you', '2019-01-05 14:59:06'),
(6, 4, 1, 'hi', '2019-01-09 08:37:40'),
(7, 1, 4, 'heyyy', '2019-01-09 08:47:25'),
(8, 4, 1, 'hi', '2019-01-09 08:47:38'),
(9, 1, 4, 'hello', '2019-01-09 08:47:44'),
(10, 4, 1, 'hrjdj', '2019-01-09 08:47:46'),
(11, 4, 1, 'bfbf', '2019-01-09 08:47:46'),
(12, 1, 4, 'hv', '2019-01-09 08:47:49'),
(13, 1, 4, 'hello', '2019-01-09 09:24:55'),
(14, 4, 1, 'gi', '2019-01-09 09:25:00'),
(15, 15, 4, 'Hellooooo', '2019-01-16 06:06:08'),
(16, 1, 4, 'aaaaaatttttttttttt', '2019-01-16 17:18:26'),
(17, 1, 4, 'naaaaayer', '2019-01-16 17:18:59'),
(18, 1, 4, 'naaaaayeraaaaaaa', '2019-01-16 17:19:54'),
(19, 1, 4, 'naaaaayeraaaaaaatttttttttt', '2019-01-16 17:20:02'),
(20, 1, 4, 'naaaaayeraaaaaaatttttttttthhhh', '2019-01-16 17:20:12'),
(21, 1, 4, 'heyyyyyy', '2019-01-16 18:02:28'),
(22, 4, 1, 'aaaaaaaaaa', '2019-01-16 18:07:13'),
(23, 1, 4, 'kkkkkkk', '2019-01-16 19:09:01'),
(24, 4, 1, 'ppppp', '2019-01-16 19:10:27'),
(25, 4, 1, 'hhhhhh', '2019-01-16 19:15:45'),
(26, 1, 4, 'oki', '2019-01-16 19:16:08'),
(27, 4, 1, 'bayer ok', '2019-01-16 19:17:05'),
(28, 1, 4, 'a head ok', '2019-01-16 19:17:23'),
(29, 1, 4, 'aaaa', '2019-01-16 19:25:37'),
(30, 1, 4, 'hhhh', '2019-01-16 19:26:14'),
(31, 1, 4, 'gggg', '2019-01-16 19:26:55'),
(32, 1, 4, 'hhhhh', '2019-01-16 19:30:38'),
(33, 1, 4, 'yyyyy', '2019-01-16 19:32:59'),
(34, 1, 4, 'tttttttttt', '2019-01-16 19:36:15'),
(35, 1, 4, 'hhhhh', '2019-01-16 19:38:22'),
(36, 1, 4, 'tttttt', '2019-01-16 19:58:32'),
(37, 1, 4, 'hhhhh', '2019-01-16 19:58:36'),
(38, 1, 4, 'aaaaa', '2019-01-16 20:05:47'),
(39, 1, 4, 'aaaaaa', '2019-01-16 20:05:49'),
(40, 1, 4, 'aaaaaa', '2019-01-16 20:05:51'),
(41, 1, 4, 'hi', '2019-01-18 08:47:30'),
(42, 4, 1, 'yo', '2019-01-18 08:53:42'),
(43, 1, 4, 'hi', '2019-01-18 08:54:04'),
(44, 1, 4, 'hiiiiiii', '2019-01-18 08:54:49'),
(45, 4, 1, 'eeee', '2019-01-18 08:55:33'),
(46, 1, 4, 'ffff', '2019-01-18 08:55:47'),
(47, 4, 1, 'eeeee', '2019-01-18 08:56:18'),
(48, 1, 4, 'hello test', '2019-01-18 09:14:36'),
(49, 4, 1, 'hello tetst', '2019-01-18 10:37:11'),
(50, 1, 4, 'hahahaha', '2019-01-18 10:37:30'),
(51, 4, 1, 'ggggggggggg', '2019-01-18 10:37:46');

-- --------------------------------------------------------

--
-- Structure de la table `notification`
--

CREATE TABLE `notification` (
  `id` int(11) NOT NULL,
  `user_id` int(11) DEFAULT NULL,
  `from_user` int(11) DEFAULT NULL,
  `created` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `project`
--

CREATE TABLE `project` (
  `id` int(11) NOT NULL,
  `label` varchar(100) DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL,
  `place` varchar(20) DEFAULT NULL,
  `description` text,
  `start_date` varchar(20) DEFAULT NULL,
  `end_date` varchar(20) DEFAULT NULL,
  `still_going` tinyint(1) DEFAULT NULL,
  `establishment_name` varchar(50) DEFAULT NULL,
  `cv_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `project`
--

INSERT INTO `project` (`id`, `label`, `type`, `place`, `description`, `start_date`, `end_date`, `still_going`, `establishment_name`, `cv_id`) VALUES
(1, 'Internship', NULL, NULL, 'aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa', '01/07/2015', '30/07/2015', 0, 'Cynapsis', 13),
(2, 'Internship', NULL, NULL, 'bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb', '10/08/2017', '10/09/2017', 0, 'FIS', 13),
(3, 'aaa', 'sss', 'qss', 'qss', '1/2019', '11/2019', 1, 'aqq', 13);

-- --------------------------------------------------------

--
-- Structure de la table `skill`
--

CREATE TABLE `skill` (
  `id` int(11) NOT NULL,
  `label` varchar(50) DEFAULT NULL,
  `description` text,
  `cv_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `skill`
--

INSERT INTO `skill` (`id`, `label`, `description`, `cv_id`) VALUES
(1, 'J2EE', NULL, 13),
(2, 'PHP', NULL, 13);

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `last_name` varchar(100) DEFAULT NULL,
  `birth_date` varchar(20) DEFAULT NULL,
  `gender` varchar(50) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `adress` varchar(250) DEFAULT NULL,
  `tel1` varchar(50) DEFAULT NULL,
  `tel2` varchar(50) DEFAULT NULL,
  `fax` varchar(50) DEFAULT NULL,
  `nationality` varchar(50) DEFAULT NULL,
  `description` text,
  `picture` varchar(250) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  `password` varchar(250) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `user`
--

INSERT INTO `user` (`id`, `name`, `last_name`, `birth_date`, `gender`, `email`, `adress`, `tel1`, `tel2`, `fax`, `nationality`, `description`, `picture`, `type`, `password`) VALUES
(1, 'Achraf', 'Abdennadher', NULL, NULL, 'a', 'Sfax, rue de teniour', '+216 53971638', NULL, NULL, 'Tunisia', 'I\'m a renewable energy executive with 10 years of experience, annnnnnd have built strong skills in getting companies', '9f37c2807b97f555a11e4abe66a01deb.png', 'a', '1111'),
(4, 'Vermeg', NULL, NULL, NULL, 'v', 'Tunis, Lac', '55858585', NULL, '8583251', 'Germany', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.', 'profile-default.png', 'c', '0000'),
(15, 'Firas', 'Cherif', NULL, NULL, 'f', NULL, NULL, NULL, NULL, 'Egypt', NULL, 'profile-default.png', 'a', '1111'),
(16, 'Hamdi', 'Hello', NULL, NULL, 'hamdi@esprit.tn', NULL, NULL, NULL, NULL, 'China', NULL, 'profile-default.png', 'a', '1111'),
(17, 'Hello', 'World', NULL, NULL, 'hello@esprit.tn', NULL, NULL, NULL, NULL, 'China', NULL, 'profile-default.png', 'c', '0000'),
(21, 'Achraf', 'Abdennadher', NULL, NULL, 'achraf.abdennadher.94@gmail.com', NULL, NULL, NULL, NULL, 'Tunnisia', NULL, 'https://media.licdn.com/dms/image/C5603AQFzQHw5Mt_ODw/profile-displayphoto-shrink_100_100/0', 'a', 'null');

-- --------------------------------------------------------

--
-- Structure de la table `user_internship`
--

CREATE TABLE `user_internship` (
  `id_user` int(11) NOT NULL,
  `id_internship` int(11) NOT NULL,
  `creation_date` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `user_internship`
--

INSERT INTO `user_internship` (`id_user`, `id_internship`, `creation_date`) VALUES
(1, 5, '18/01/2019'),
(1, 7, '18/01/2019');

-- --------------------------------------------------------

--
-- Structure de la table `user_job`
--

CREATE TABLE `user_job` (
  `id_user` int(11) NOT NULL,
  `id_job` int(11) NOT NULL,
  `creation_date` varchar(50) NOT NULL,
  `state` varchar(20) DEFAULT 'p'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `user_job`
--

INSERT INTO `user_job` (`id_user`, `id_job`, `creation_date`, `state`) VALUES
(1, 9, '18/01/2019', 'a');

-- --------------------------------------------------------

--
-- Structure de la table `volunteer`
--

CREATE TABLE `volunteer` (
  `id` int(11) NOT NULL,
  `organisation` varchar(50) DEFAULT NULL,
  `role` varchar(50) DEFAULT NULL,
  `domain` varchar(20) DEFAULT NULL,
  `start_date` varchar(20) DEFAULT NULL,
  `end_date` varchar(20) DEFAULT NULL,
  `still_going` tinyint(1) DEFAULT NULL,
  `description` text,
  `cv_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Déchargement des données de la table `volunteer`
--

INSERT INTO `volunteer` (`id`, `organisation`, `role`, `domain`, `start_date`, `end_date`, `still_going`, `description`, `cv_id`) VALUES
(1, 'JCI Teniour', 'Member', NULL, '30/09/2014', '30/07/2015', 0, NULL, 13);

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `certification`
--
ALTER TABLE `certification`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_cv8_id` (`cv_id`);

--
-- Index pour la table `cv`
--
ALTER TABLE `cv`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_app5_id` (`user_id`);

--
-- Index pour la table `education`
--
ALTER TABLE `education`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_cv0_id` (`cv_id`);

--
-- Index pour la table `internship`
--
ALTER TABLE `internship`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_comp_int9` (`user_id`);

--
-- Index pour la table `job`
--
ALTER TABLE `job`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_comp_id20` (`user_id`);

--
-- Index pour la table `language`
--
ALTER TABLE `language`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_cv7_id` (`cv_id`);

--
-- Index pour la table `message`
--
ALTER TABLE `message`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_sender` (`sender`),
  ADD KEY `fk_receiver` (`receiver`);

--
-- Index pour la table `notification`
--
ALTER TABLE `notification`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `project`
--
ALTER TABLE `project`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_cv10_id` (`cv_id`);

--
-- Index pour la table `skill`
--
ALTER TABLE `skill`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_prj1_id` (`cv_id`);

--
-- Index pour la table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `user_internship`
--
ALTER TABLE `user_internship`
  ADD PRIMARY KEY (`id_user`,`id_internship`),
  ADD KEY `fk_id_int20` (`id_internship`),
  ADD KEY `fk_id_app20` (`id_user`);

--
-- Index pour la table `user_job`
--
ALTER TABLE `user_job`
  ADD PRIMARY KEY (`id_user`,`id_job`),
  ADD KEY `fk_id_job15` (`id_job`),
  ADD KEY `fk_id_app15` (`id_user`);

--
-- Index pour la table `volunteer`
--
ALTER TABLE `volunteer`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_cv9_id` (`cv_id`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `certification`
--
ALTER TABLE `certification`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `cv`
--
ALTER TABLE `cv`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- AUTO_INCREMENT pour la table `education`
--
ALTER TABLE `education`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT pour la table `internship`
--
ALTER TABLE `internship`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT pour la table `job`
--
ALTER TABLE `job`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;

--
-- AUTO_INCREMENT pour la table `language`
--
ALTER TABLE `language`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `message`
--
ALTER TABLE `message`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=52;

--
-- AUTO_INCREMENT pour la table `notification`
--
ALTER TABLE `notification`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `project`
--
ALTER TABLE `project`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT pour la table `skill`
--
ALTER TABLE `skill`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT pour la table `volunteer`
--
ALTER TABLE `volunteer`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `certification`
--
ALTER TABLE `certification`
  ADD CONSTRAINT `fk_cv8_id` FOREIGN KEY (`cv_id`) REFERENCES `cv` (`id`) ON DELETE CASCADE;

--
-- Contraintes pour la table `cv`
--
ALTER TABLE `cv`
  ADD CONSTRAINT `fk_app5_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE;

--
-- Contraintes pour la table `education`
--
ALTER TABLE `education`
  ADD CONSTRAINT `fk_cv0_id` FOREIGN KEY (`cv_id`) REFERENCES `cv` (`id`) ON DELETE CASCADE;

--
-- Contraintes pour la table `internship`
--
ALTER TABLE `internship`
  ADD CONSTRAINT `fk_comp_int9` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE;

--
-- Contraintes pour la table `job`
--
ALTER TABLE `job`
  ADD CONSTRAINT `fk_comp_id20` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE;

--
-- Contraintes pour la table `language`
--
ALTER TABLE `language`
  ADD CONSTRAINT `fk_cv7_id` FOREIGN KEY (`cv_id`) REFERENCES `cv` (`id`) ON DELETE CASCADE;

--
-- Contraintes pour la table `message`
--
ALTER TABLE `message`
  ADD CONSTRAINT `fk_receiver` FOREIGN KEY (`receiver`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_sender` FOREIGN KEY (`sender`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Contraintes pour la table `project`
--
ALTER TABLE `project`
  ADD CONSTRAINT `fk_cv10_id` FOREIGN KEY (`cv_id`) REFERENCES `cv` (`id`) ON DELETE CASCADE;

--
-- Contraintes pour la table `skill`
--
ALTER TABLE `skill`
  ADD CONSTRAINT `fk_prj1_id` FOREIGN KEY (`cv_id`) REFERENCES `cv` (`id`) ON DELETE CASCADE;

--
-- Contraintes pour la table `user_internship`
--
ALTER TABLE `user_internship`
  ADD CONSTRAINT `fk_id_app20` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_id_int20` FOREIGN KEY (`id_internship`) REFERENCES `internship` (`id`) ON DELETE CASCADE;

--
-- Contraintes pour la table `user_job`
--
ALTER TABLE `user_job`
  ADD CONSTRAINT `fk_id_2app` FOREIGN KEY (`id_user`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_id_job15` FOREIGN KEY (`id_job`) REFERENCES `job` (`id`) ON DELETE CASCADE;

--
-- Contraintes pour la table `volunteer`
--
ALTER TABLE `volunteer`
  ADD CONSTRAINT `fk_cv9_id` FOREIGN KEY (`cv_id`) REFERENCES `cv` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
