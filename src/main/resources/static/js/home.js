document.addEventListener('DOMContentLoaded', () => {

    const sortRadioButtons = document.querySelectorAll('input[name="sortOrder"]');
    const articleTbody = document.getElementById('article-tbody');

    if (!articleTbody) {
        return;
    }

    const rows = Array.from(articleTbody.querySelectorAll('tr'));

    const sortAndRender = (direction) => {
        rows.sort((rowA, rowB) => {
            const dateA = new Date(rowA.dataset.createdAt);
            const dateB = new Date(rowB.dataset.createdAt);
            return direction === 'desc' ? dateB - dateA : dateA - dateB;
        });

        articleTbody.innerHTML = '';
        rows.forEach(row => {
            articleTbody.appendChild(row);
        });
    };

    sortRadioButtons.forEach(radio => {
        radio.addEventListener('change', (event) => {
            sortAndRender(event.target.value);
        });
    });

    sortAndRender('desc');
});
