document.addEventListener('DOMContentLoaded', () => {

    const mainElement = document.getElementById('article-details');
    if (!mainElement) {
        console.error("id='article-details' の要素が見つかりません。");
        return;
    }
    const articleId = mainElement.dataset.articleId;

    const commentList = document.getElementById('commentList');
    const commentForm = document.getElementById('commentForm');

    if (!commentList) {
        console.error("id='commentList' の要素が見つかりません。");
        return;
    }

    if (commentForm) {
        const commentContent = document.getElementById('content');
        const commentError = document.getElementById('commentError');

        commentForm.addEventListener('submit', async (e) => {
            e.preventDefault();

            if (!commentContent || !commentError) return;

            commentError.textContent = '';
            const content = commentContent.value.trim();
            if (!content) {
                commentError.textContent = 'コメント内容は必須です。';
                return;
            }

            try {
                const response = await fetch(`/api/articles/${articleId}/comments`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ content: content }),
                });

                if (response.ok) {
                    commentContent.value = '';
                    await fetchAndRenderComments();
                } else {
                    const errorData = await response.json();
                    commentError.textContent = errorData[0]?.defaultMessage || '投稿に失敗しました。';
                }
            } catch (error) {
                commentError.textContent = '通信エラーが発生しました。';
            }
        });
    }

    // --- 関数定義 ---
    const formatDate = (dateString) => {
        const date = new Date(dateString);
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        return `${year}/${month}/${day} ${hours}:${minutes}`;
    };

    const escapeHTML = str => {
        return str.replace(/[&<>"']/g, function(match) {
            return {
                '&': '&amp;',
                '<': '&lt;',
                '>': '&gt;',
                '"': '&quot;',
                "'": '&#39;'
            }[match];
        });
    };

    const fetchAndRenderComments = async () => {
        try {
            const response = await fetch(`/api/articles/${articleId}/comments`);
            if (!response.ok) throw new Error('コメントの取得に失敗しました。');

            const comments = await response.json();
            commentList.innerHTML = '';

            if (comments.length === 0) {
                const li = document.createElement('li');
                li.textContent = 'まだコメントはありません。';
                commentList.appendChild(li);
            } else {
                comments.forEach(comment => {
                    const li = document.createElement('li');
                    li.innerHTML = `
                        <strong>${escapeHTML(comment.user.name)}</strong>
                        <small>${formatDate(comment.createdAt)}</small>
                        <p>${escapeHTML(comment.content).replace(/\n/g, '<br>')}</p>
                    `;
                    commentList.appendChild(li);
                });
            }
        } catch (error) {
            commentList.innerHTML = `<li style="color: red;">${error.message}</li>`;
        }
    };

    fetchAndRenderComments();
});
