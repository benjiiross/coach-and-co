import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./normalize.css";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
	title: {
		default: "Coach & Co | Trouve ton coach sportif personnel",
		template: "%s | Coach & Co",
	},
	description: "Mise en relation directe entre coachs certifiés et sportifs.",
	keywords: [
		"coach sportif",
		"fitness",
		"entraînement personnalisé",
		"sport mobile app",
	],
	authors: [
		{ name: "Mehdi Bakri" },
		{ name: "Paul Edouard Frenet" },
		{ name: "Raphaël Gossin" },
		{ name: "Silvère Lalöe" },
		{ name: "Nabil Rachidi" },
		{ name: "Benjamin Rossignol", url: "https://benjaminrossignol.vercel.app" },
	],
	metadataBase: new URL("https://coach-and-co.vercel.app"),
	openGraph: {
		title: "Coach & Co - Plateforme de Coaching Sportif",
		description: "La nouvelle génération du coaching sportif arrive.",
		url: "https://coach-and-co.vercel.app",
		siteName: "Coach & Co",
		locale: "fr_FR",
		type: "website",
	},
	robots: {
		index: true,
		follow: true,
	},
};

export default function RootLayout({ children }: LayoutProps<"/">) {
	return (
		<html lang="en" className="scroll-smooth">
			<body className={inter.className}>{children}</body>
		</html>
	);
}
