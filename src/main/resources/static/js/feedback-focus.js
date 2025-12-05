// A11Y : Focus automatique sur les messages d'info et d'erreur quand ils existent
// (car aria-live ne fonctionne pas pour le SSR, uniquement pour les changements asynchrones du DOM)

document.addEventListener('DOMContentLoaded', function() {
    const feedback = document.getElementById('feedback-message')
    if (feedback?.textContent.trim()) {
        feedback.focus()
    }
});