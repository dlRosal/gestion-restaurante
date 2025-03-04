-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 25-02-2025 a las 09:22:19
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `gestionpedidorestaurante`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clientes`
--

CREATE TABLE `clientes` (
  `id` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `telefono` varchar(50) NOT NULL,
  `direccion` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `clientes`
--

INSERT INTO `clientes` (`id`, `nombre`, `telefono`, `direccion`) VALUES
(1, 'Valdo', '698632654', 'C/Neftali'),
(2, 'PAco', '8798494984', 'cacdasc'),
(3, 'Rosal', '6332687123', 'C/Pintalmona'),
(4, 'Jose Luis', '623541240', 'C/Bocanegra'),
(5, 'Manolito', '65322415', 'C/Cortijuelos'),
(6, 'Ivan David', '698631239', 'C/Colombino');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detallepedidos`
--

CREATE TABLE `detallepedidos` (
  `idDetallePedido` int(11) NOT NULL,
  `idPedido` int(11) NOT NULL,
  `idProducto` int(11) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `precio` decimal(10,2) NOT NULL,
  `subtotal` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `detallepedidos`
--

INSERT INTO `detallepedidos` (`idDetallePedido`, `idPedido`, `idProducto`, `cantidad`, `precio`, `subtotal`) VALUES
(7, 29, 11, 2, 4.00, 8.00),
(13, 31, 7, 2, 6.95, 13.90),
(15, 31, 6, 8, 6.95, 55.60),
(17, 31, 8, 3, 6.95, 20.85),
(18, 33, 7, 2, 6.95, 13.90),
(19, 34, 8, 2, 6.95, 13.90),
(21, 35, 11, 2, 4.00, 8.00),
(22, 36, 12, 5, 2.00, 10.00),
(24, 32, 11, 3, 4.00, 12.00);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `pedidos`
--

CREATE TABLE `pedidos` (
  `idPedido` int(11) NOT NULL,
  `idCliente` int(11) NOT NULL,
  `fechaPedido` date NOT NULL,
  `horaPedido` time NOT NULL,
  `total` double NOT NULL DEFAULT 0,
  `estado` enum('Pendiente','En preparación','Entregado') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `pedidos`
--

INSERT INTO `pedidos` (`idPedido`, `idCliente`, `fechaPedido`, `horaPedido`, `total`, `estado`) VALUES
(29, 4, '2025-02-21', '12:56:00', 8, 'Pendiente'),
(30, 3, '2025-02-22', '12:53:00', 62.64999999999999, 'Pendiente'),
(31, 4, '2025-02-06', '12:36:00', 90.35, 'Pendiente'),
(32, 2, '2025-02-20', '12:30:00', 12, 'Pendiente'),
(33, 4, '2025-02-06', '12:36:00', 13.9, 'Pendiente'),
(34, 4, '2025-02-02', '12:45:00', 13.9, 'Pendiente'),
(35, 5, '2025-02-27', '14:30:00', 8, 'Pendiente'),
(36, 6, '2025-02-14', '21:30:00', 10, 'Pendiente');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productos`
--

CREATE TABLE `productos` (
  `id` int(11) NOT NULL,
  `nombre` varchar(50) NOT NULL,
  `categoria` varchar(50) NOT NULL,
  `precio` varchar(50) NOT NULL,
  `disponibilidad` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `productos`
--

INSERT INTO `productos` (`id`, `nombre`, `categoria`, `precio`, `disponibilidad`) VALUES
(5, 'Crazy Bacon (2 carnes)', 'Burger', '8.95', '1'),
(6, 'Crazy Bacon (1 carnes)', 'Burger', '6.95', '1'),
(7, 'Brutal Bacon', 'Burger', '6.95', '1'),
(8, 'Duo Bacon', 'Burger', '6.95', '1'),
(9, 'Crazy Bacon', 'Burger', '8.95', '1'),
(10, 'Duo Bacon', 'Burger', '6.55', '1'),
(11, 'Variado Pollo', 'Pollo', '4.0', '1'),
(12, 'Arepa de queso', 'Arepas', '2.0', '1'),
(13, 'Colombiana', 'Bebidas', '1.0', '1');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `clientes`
--
ALTER TABLE `clientes`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `detallepedidos`
--
ALTER TABLE `detallepedidos`
  ADD PRIMARY KEY (`idDetallePedido`),
  ADD KEY `idPedido` (`idPedido`),
  ADD KEY `idProducto` (`idProducto`);

--
-- Indices de la tabla `pedidos`
--
ALTER TABLE `pedidos`
  ADD PRIMARY KEY (`idPedido`),
  ADD KEY `idCliente` (`idCliente`);

--
-- Indices de la tabla `productos`
--
ALTER TABLE `productos`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `clientes`
--
ALTER TABLE `clientes`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `detallepedidos`
--
ALTER TABLE `detallepedidos`
  MODIFY `idDetallePedido` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT de la tabla `pedidos`
--
ALTER TABLE `pedidos`
  MODIFY `idPedido` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;

--
-- AUTO_INCREMENT de la tabla `productos`
--
ALTER TABLE `productos`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `detallepedidos`
--
ALTER TABLE `detallepedidos`
  ADD CONSTRAINT `detallepedidos_ibfk_1` FOREIGN KEY (`idPedido`) REFERENCES `pedidos` (`idPedido`) ON DELETE CASCADE,
  ADD CONSTRAINT `detallepedidos_ibfk_2` FOREIGN KEY (`idProducto`) REFERENCES `productos` (`id`) ON DELETE CASCADE;

--
-- Filtros para la tabla `pedidos`
--
ALTER TABLE `pedidos`
  ADD CONSTRAINT `pedidos_ibfk_1` FOREIGN KEY (`idCliente`) REFERENCES `clientes` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
