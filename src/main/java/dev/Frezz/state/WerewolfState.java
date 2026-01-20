package dev.Frezz.state;

/**
 * Représente l'état loup-garou d'un joueur.
 * Un joueur peut être humain, loup-garou, ou les deux dans le futur.
 */
public class WerewolfState {

    private boolean isWerewolf;
    private boolean isTransformed;

    public WerewolfState() {
        this.isWerewolf = false;
        this.isTransformed = false;
    }

    /**
     * Vérifie si le joueur est un loup-garou.
     */
    public boolean isWerewolf() {
        return isWerewolf;
    }

    /**
     * Définit si le joueur est un loup-garou.
     */
    public void setWerewolf(boolean werewolf) {
        this.isWerewolf = werewolf;
        if (!werewolf) {
            this.isTransformed = false;
        }
    }

    /**
     * Vérifie si le joueur est actuellement transformé en loup-garou.
     */
    public boolean isTransformed() {
        return isTransformed;
    }

    /**
     * Définit si le joueur est transformé.
     * Ne peut être true que si le joueur est un loup-garou.
     */
    public void setTransformed(boolean transformed) {
        if (isWerewolf) {
            this.isTransformed = transformed;
        }
    }

    /**
     * Bascule l'état de transformation.
     * @return true si la transformation a réussi, false sinon.
     */
    public boolean toggleTransformation() {
        if (!isWerewolf) {
            return false;
        }
        this.isTransformed = !this.isTransformed;
        return true;
    }

    /**
     * Retourne une description textuelle du statut.
     */
    public String getStatusDescription() {
        if (!isWerewolf) {
            return "Humain";
        }
        return isTransformed ? "Loup-Garou (Transformé)" : "Loup-Garou (Forme Humaine)";
    }
}

