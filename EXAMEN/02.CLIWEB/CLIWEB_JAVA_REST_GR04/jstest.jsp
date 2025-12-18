<script>
    (() => {
        const onReady = (callback) => {
            if (document.readyState === 'loading') {
                document.addEventListener('DOMContentLoaded', callback, { once: true });
            } else {
                callback();
            }
        };

        onReady(() => {
            const modal = document.getElementById('productModal');
            const selectButton = document.getElementById('selectProductButton');
            const closeButton = document.getElementById('closeProductModal');
            const productCards = modal ? Array.from(modal.querySelectorAll('.eq-product-card')) : [];
            const productIdInput = document.getElementById('productoIdInput');
            const placeholder = document.getElementById('selectedProductEmpty');
            const details = document.getElementById('selectedProductDetails');
            const thumbIcon = document.getElementById('selectedProductThumbIcon');
            const thumbImg = document.getElementById('selectedProductThumbImg');
            const nameEl = document.getElementById('selectedProductName');
            const codeEl = document.getElementById('selectedProductCode');
            const priceEl = document.getElementById('selectedProductPrice');
            const quantityInput = document.getElementById('cantidadInput');
            const addButton = document.getElementById('addProductButton');
            const cartList = document.getElementById('cartItemsList');
            const cartEmptyMessage = document.getElementById('cartEmptyMessage');
            const itemsStateInput = document.getElementById('itemsStateInput');
            const form = document.getElementById('facturaForm');

            const productsMap = new Map();
            productCards.forEach((card) => {
                const data = {
                    id: card.dataset.productId,
                    name: card.dataset.productName,
                    code: card.dataset.productCode,
                    price: Number(card.dataset.productPrice || 0),
                    image: card.dataset.productImage || ''
                };
                productsMap.set(data.id, data);
            });

            let selectedProduct = null;
            let cartItems = [];

            const formatPrice = (value) => {
                const number = Number(value);
                if (Number.isNaN(number)) {
                    return '$0.00';
                }
                return new Intl.NumberFormat('es-EC', {
                    style: 'currency',
                    currency: 'USD',
                    minimumFractionDigits: 2
                }).format(number);
            };

            const setThumbImage = (source) => {
                if (!thumbImg || !thumbIcon) {
                    return;
                }
                if (source) {
                    thumbImg.src = source;
                    thumbImg.hidden = false;
                    thumbIcon.style.display = 'none';
                } else {
                    thumbImg.hidden = true;
                    thumbIcon.style.display = 'inline-flex';
                }
            };

            const updateSelectedUi = (product) => {
                if (!details || !placeholder) {
                    return;
                }
                if (!product) {
                    details.hidden = true;
                    placeholder.hidden = false;
                    setThumbImage('');
                    if (nameEl) {
                        nameEl.textContent = 'Producto';
                    }
                    if (codeEl) {
                        codeEl.textContent = 'C\u00F3digo';
                    }
                    if (priceEl) {
                        priceEl.textContent = '$0.00';
                    }
                    return;
                }
                details.hidden = false;
                placeholder.hidden = true;
                setThumbImage(product.image);
                if (nameEl) {
                    nameEl.textContent = product.name || 'Producto ElectroQuito';
                }
                if (codeEl) {
                    codeEl.textContent = product.code
                        ? 'C\u00F3digo: ' + product.code
                        : 'C\u00F3digo no disponible';
                }
                if (priceEl) {
                    priceEl.textContent = formatPrice(product.price);
                }
            };

            const setSelectedProduct = (product) => {
                selectedProduct = product || null;
                if (productIdInput) {
                    productIdInput.value = selectedProduct ? selectedProduct.id || '' : '';
                }
                updateSelectedUi(selectedProduct);
                if (addButton) {
                    addButton.disabled = !selectedProduct;
                }
            };

            const syncItemsState = () => {
                if (!itemsStateInput) {
                    return;
                }
                const payload = cartItems.map((item) => ({
                    idElectrodomestico: Number(item.id),
                    cantidad: item.cantidad
                }));
                itemsStateInput.value = JSON.stringify(payload);
            };

            const renderCart = () => {
                if (!cartList || !cartEmptyMessage) {
                    return;
                }
                cartList.innerHTML = '';
                if (cartItems.length === 0) {
                    cartEmptyMessage.hidden = false;
                    return;
                }
                cartEmptyMessage.hidden = true;
                cartItems.forEach((item, index) => {
                    const product = productsMap.get(item.id) || {};
                    const subtotal = (product.price || 0) * item.cantidad;
                    const wrapper = document.createElement('div');
                    wrapper.className = 'eq-cart-item';
                    wrapper.innerHTML = `
                        <div class="eq-cart-item-info">
                            <p class="eq-cart-item-title">${product.name || 'Producto seleccionado'}</p>
                            <p class="eq-cart-item-meta">
                                ${product.code ? 'C&oacute;digo: ' + product.code + ' - ' : ''}
                                <span class="eq-cart-item-qty">Cantidad: ${item.cantidad}</span>
                            </p>
                        </div>
                        <p class="eq-cart-item-subtotal">${formatPrice(subtotal)}</p>
                        <button type="button"
                                class="eq-cart-remove"
                                data-index="${index}">
                            <span class="material-icons-round">close</span>
                        </button>
                    `;
                    cartList.appendChild(wrapper);
                });
            };

            const loadInitialItems = () => {
                if (!itemsStateInput) {
                    return;
                }
                try {
                    const parsed = JSON.parse(itemsStateInput.value || '[]');
                    if (Array.isArray(parsed)) {
                        cartItems = parsed
                            .filter((item) => item && item.idElectrodomestico)
                            .map((item) => ({
                                id: String(item.idElectrodomestico),
                                cantidad: Number(item.cantidad) > 0 ? Number(item.cantidad) : 1
                            }));
                    }
                } catch (err) {
                    cartItems = [];
                }
                renderCart();
                syncItemsState();
            };

            const addCurrentProduct = () => {
                if (!selectedProduct) {
                    alert('Selecciona un producto del cat\u00E1logo antes de agregarlo.');
                    return;
                }
                const inputValue = quantityInput ? Number(quantityInput.value) : 1;
                const quantity = Math.max(1, Number.isNaN(inputValue) ? 1 : inputValue);
                const existing = cartItems.find((item) => item.id === selectedProduct.id);
                if (existing) {
                    existing.cantidad += quantity;
                } else {
                    cartItems.push({
                        id: selectedProduct.id,
                        cantidad: quantity
                    });
                }
                if (quantityInput) {
                    quantityInput.value = 1;
                }
                renderCart();
                syncItemsState();
            };

            const openModal = () => {
                if (!modal || (selectButton && selectButton.disabled)) {
                    return;
                }
                modal.classList.add('is-active');
                if (document.body) {
                    document.body.classList.add('eq-modal-open');
                }
            };

            const closeModal = () => {
                if (!modal) {
                    return;
                }
                modal.classList.remove('is-active');
                if (document.body) {
                    document.body.classList.remove('eq-modal-open');
                }
            };

            if (selectButton) {
                selectButton.addEventListener('click', openModal);
            }

            if (closeButton) {
                closeButton.addEventListener('click', closeModal);
            }

            if (modal) {
                modal.addEventListener('click', (event) => {
                    if (event.target === modal) {
                        closeModal();
                    }
                });
            }

            document.addEventListener('keydown', (event) => {
                if (event.key === 'Escape' && modal && modal.classList.contains('is-active')) {
                    closeModal();
                }
            });

            productCards.forEach((card) => {
                card.addEventListener('click', () => {
                    const product = productsMap.get(card.dataset.productId);
                    setSelectedProduct(product || null);
                    closeModal();
                });
            });

            if (addButton) {
                addButton.addEventListener('click', addCurrentProduct);
            }

            if (cartList) {
                cartList.addEventListener('click', (event) => {
                    const button = event.target.closest('.eq-cart-remove');
                    if (!button) {
                        return;
                    }
                    const index = Number(button.dataset.index);
                    if (Number.isNaN(index)) {
                        return;
                    }
                    cartItems.splice(index, 1);
                    renderCart();
                    syncItemsState();
                });
            }

            if (form) {
                form.addEventListener('submit', (event) => {
                    if (cartItems.length === 0) {
                        event.preventDefault();
                        alert('Agrega al menos un producto antes de generar la factura.');
                        return;
                    }
                    syncItemsState();
                });
            }

            if (form && form.dataset.hasSuccess === 'true') {
                form.reset();
                Array.from(form.querySelectorAll('input[type="text"]')).forEach((input) => {
                    input.value = '';
                });
                if (quantityInput) {
                    quantityInput.value = 1;
                }
                const efectivo = form.querySelector('input[name="formaPago"][value="EFECTIVO"]');
                if (efectivo) {
                    efectivo.checked = true;
                }
                cartItems = [];
                renderCart();
                syncItemsState();
                setSelectedProduct(null);
            } else {
                loadInitialItems();
                if (productIdInput && productIdInput.value) {
                    const preset = productsMap.get(productIdInput.value);
                    setSelectedProduct(preset || null);
                } else {
                    setSelectedProduct(null);
                }
            }
        });
    })();
</script>
