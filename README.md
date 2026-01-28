# Coach & Co 🏃‍♂️

**La plateforme moderne de mise en relation entre coachs sportifs et athlètes.**

## 🚀 Vision du Projet

Coach & Co simplifie la recherche de coaching personnalisé grâce à une expérience fluide sur mobile et une présence web optimisée pour le SEO. Ce dépôt contient l'architecture complète du projet.

---

## 🛠 Stack Technique

### Mobile (Android & iOS)

- **Framework:** [Kotlin Multiplatform (KMP)](https://kotlinlang.org/docs/multiplatform.html)
- **UI:** Compose Multiplatform (Partage d'interface 100% natif)
- **Architecture:** MVVM avec logique partagée dans le module `:shared`

### Web (Landing Page & SEO)

- **Framework:** [Next.js](https://nextjs.org/) (App Router)
- **Optimisation:** React Compiler activé par défaut.
- **Styling:** Tailwind CSS.
- **SEO:** Server-Side Rendering (SSR) pour une indexation maximale des profils de coachs.

---

## 📁 Structure du Repo

```text
.
├── mobile/             # Projet Kotlin Multiplatform
│   ├── composeApp/     # Code UI partagé (Android/iOS)
│   └── shared/         # Logique métier, API & Validation
├── web/                # Site Next.js (Dossier /src)
└── README.md
```

## ⚙️ Installation & Lancement

### Web

```bash
cd web
npm install
npm run dev
```

### Mobile

- Ouvrez le dossier mobile dans Android Studio.
- Sélectionnez la cible (Android Emulator ou iOS Simulator).
- Run.

## 📸 Showcase & Statut

Ce dépôt est une version de démonstration de l'architecture.

- [x] Structure Monorepo
- [x] Configuration Next.js (SEO ready)
- [ ] Intégration API Backend (En cours)
- [ ] Paiements Stripe (Prévu)

*Développé avec passion pour le sport et le code.*
