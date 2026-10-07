<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<header class="bg-white border-b border-slate-200 sticky top-0 z-50">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 h-18 flex items-center justify-between gap-4 py-3">
      
      <!-- LOGO SÀN GIAO DỊCH B2C MARKET CREATOR -->
      <div class="flex items-center gap-3 shrink-0">
        <a href="01-trang-chu.jsp" class="flex items-center gap-2.5">
          <img src="assets/logos/muangay-logo-icon.svg" alt="MuaNgay Logo" class="w-10 h-10 rounded-xl shadow-xs">
          <div>
            <span class="text-xl font-extrabold text-slate-900 tracking-tight block">Mua<span class="text-blue-600">Ngay</span></span>
            <span class="text-[11px] text-slate-500 block -mt-0.5 font-medium">Cần là Mua Ngay, thừa thì Pass Luôn</span>
          </div>
        </a>
      </div>

      <!-- Ô TÌM KIẾM TỪ KHÓA ĐA NĂNG (TÍCH HỢP TÌM KIẾM HOẠT ĐỘNG) -->
      <form id="searchForm" action="search" method="get" class="flex-1 max-w-lg hidden sm:block">
        <div class="relative">
          <input 
            id="searchInput"
            name="q"
            type="text" 
            placeholder="Tìm xe máy, iPhone, tủ lạnh, bàn ghế, laptop, máy ảnh..." 
            autocomplete="off"
            aria-label="Tìm kiếm sản phẩm"
            aria-expanded="false"
            aria-controls="searchPanel"
            class="w-full pl-4 pr-24 py-2 bg-slate-100 border border-slate-200 rounded-lg text-xs focus:outline-none focus:ring-2 focus:ring-blue-500 focus:bg-white transition"
          >
          <button type="submit" class="absolute right-1 top-1 bottom-1 px-3 bg-blue-600 text-white rounded-md text-xs font-semibold hover:bg-blue-700 transition">
            Tìm kiếm
          </button>
          <div id="searchPanel" class="hidden absolute left-0 right-0 top-full mt-2 bg-white border border-slate-200 rounded-xl shadow-xl z-[60] overflow-hidden" role="region" aria-label="Đề xuất và lịch sử tìm kiếm">
            <div id="suggestionSection" class="hidden"></div>
            <div id="historySection" class="hidden border-t border-slate-100"></div>
          </div>
        </div>
      </form>

      <!-- BỘ CHỌN KHU VỰC ĐỊA LÝ (TỈNH / THÀNH PHỐ) -->
      <div class="hidden lg:flex items-center">
        <select id="provinceSelect" onchange="applyFilters()" class="bg-slate-100 border border-slate-200 text-xs font-semibold rounded-lg px-2.5 py-2 text-slate-700 focus:outline-none focus:ring-2 focus:ring-blue-500 cursor-pointer">
          <option value="all">Toàn Quốc</option>
          <option value="hcm" selected>TP. Hồ Chí Minh</option>
          <option value="hn">Hà Nội</option>
          <option value="dn">Đà Nẵng</option>
          <option value="bd">Bình Dương</option>
          <option value="ct">Cần Thơ</option>
        </select>
      </div>

      <!-- CỤM TÀI KHOẢN, QUẢN LÝ ĐƠN HÀNG VÀ ĐĂNG TIN -->
      <div class="flex items-center gap-2 sm:gap-3">
        <a href="07-dang-nhap-xac-thuc.jsp" class="hidden sm:inline-block px-2 py-1 text-xs text-slate-600 hover:text-blue-600 font-semibold">
          Đăng nhập
        </a>
        <a href="05-quan-ly-don-hang.jsp" class="px-2.5 py-1.5 text-xs font-semibold text-slate-700 hover:text-blue-600 hover:bg-slate-100 rounded-lg transition border border-slate-200">
          Đơn hàng (2)
        </a>
        <a href="04-chat-tra-gia-vietqr.jsp" class="px-2.5 py-1.5 text-xs font-semibold text-slate-700 hover:text-blue-600 hover:bg-slate-100 rounded-lg transition relative">
          Tin nhắn
          <span class="inline-block w-2 h-2 rounded-full bg-red-500 ml-0.5"></span>
        </a>
        <a href="06-quan-tri-admin.jsp" class="hidden md:inline-block px-2.5 py-1.5 text-xs font-semibold text-purple-700 hover:bg-purple-50 rounded-lg transition border border-purple-200">
          Admin Sàn
        </a>
        <a href="03-dang-tin.jsp" class="px-3.5 py-2 bg-amber-500 hover:bg-amber-600 text-slate-900 text-xs font-bold rounded-lg shadow-sm transition">
          Đăng Tin Miễn Phí
        </a>
        <a href="08-ho-so-ca-nhan.jsp" title="Hồ sơ cá nhân" class="w-8 h-8 rounded-full bg-blue-600 text-white flex items-center justify-center text-xs font-bold hover:ring-2 hover:ring-blue-400 transition shrink-0">
          VB
        </a>
      </div>

    </div>
  </header>
  <script>
    (() => {
      const form = document.getElementById('searchForm');
      const input = document.getElementById('searchInput');
      const panel = document.getElementById('searchPanel');
      if (!form || !input || !panel) return;

      const historyKey = 'muangay-search-history';
      const suggestionSection = document.getElementById('suggestionSection');
      const historySection = document.getElementById('historySection');
      let suggestionRequest = 0;
      let suggestionTimer;
      const getHistory = () => {
        try { return JSON.parse(localStorage.getItem(historyKey) || '[]').filter(Boolean).slice(0, 5); }
        catch (_) { return []; }
      };
      const saveSearch = (value) => {
        const query = value.trim();
        if (!query) return;
        const history = [query, ...getHistory().filter(item => item.toLocaleLowerCase() !== query.toLocaleLowerCase())].slice(0, 5);
        try { localStorage.setItem(historyKey, JSON.stringify(history)); } catch (_) { /* Storage may be disabled. */ }
      };
      const openPanel = () => {
        panel.classList.remove('hidden');
        input.setAttribute('aria-expanded', 'true');
      };
      const closePanel = () => {
        panel.classList.add('hidden');
        input.setAttribute('aria-expanded', 'false');
      };
      const makeRow = (label, detail, onClick, icon) => {
        const button = document.createElement('button');
        button.type = 'button';
        button.className = 'w-full px-4 py-2.5 text-left hover:bg-slate-50 flex items-center gap-3';
        const symbol = document.createElement('span');
        symbol.className = 'text-slate-400 text-sm w-4 text-center';
        symbol.textContent = icon;
        const textWrap = document.createElement('span');
        textWrap.className = 'min-w-0 flex-1';
        const title = document.createElement('span');
        title.className = 'block text-xs font-medium text-slate-800 truncate';
        title.textContent = label;
        textWrap.appendChild(title);
        if (detail) {
          const subtitle = document.createElement('span');
          subtitle.className = 'block text-[11px] text-slate-500 truncate mt-0.5';
          subtitle.textContent = detail;
          textWrap.appendChild(subtitle);
        }
        button.append(symbol, textWrap);
        button.addEventListener('click', onClick);
        return button;
      };
      const renderPanel = async () => {
        const query = input.value.trim();
        const currentRequest = ++suggestionRequest;
        suggestionSection.replaceChildren();
        historySection.replaceChildren();
        let matches = [];
        if (query.length >= 2) {
          try {
            const endpoint = new URL('search/suggestions', document.baseURI);
            endpoint.searchParams.set('q', query);
            const response = await fetch(endpoint, { headers: { 'Accept': 'application/json' } });
            if (!response.ok) throw new Error('Không tải được gợi ý tìm kiếm');
            matches = await response.json();
          } catch (_) {
            matches = [];
          }
          if (currentRequest !== suggestionRequest) return;
        }

        if (matches.length) {
          const heading = document.createElement('div');
          heading.className = 'px-4 pt-3 pb-1 text-[10px] uppercase tracking-wide font-bold text-slate-400';
          heading.textContent = 'Sản phẩm phù hợp';
          suggestionSection.appendChild(heading);
          matches.forEach(product => {
            const title = product.title || '';
            const price = new Intl.NumberFormat('vi-VN').format(product.price || 0) + ' đ';
            suggestionSection.appendChild(makeRow(title, price, () => {
              input.value = title;
              closePanel();
              form.requestSubmit();
            }, '⌕'));
          });
          suggestionSection.classList.remove('hidden');
        } else {
          suggestionSection.classList.add('hidden');
        }

        const history = getHistory();
        if (history.length) {
          const headingRow = document.createElement('div');
          headingRow.className = 'px-4 pt-3 pb-1 flex items-center justify-between';
          const heading = document.createElement('span');
          heading.className = 'text-[10px] uppercase tracking-wide font-bold text-slate-400';
          heading.textContent = 'Lịch sử tìm kiếm';
          const clear = document.createElement('button');
          clear.type = 'button';
          clear.className = 'text-[11px] font-medium text-blue-600 hover:text-blue-700';
          clear.textContent = 'Xóa lịch sử';
          clear.addEventListener('click', () => {
            try { localStorage.removeItem(historyKey); } catch (_) { /* Storage may be disabled. */ }
            renderPanel();
          });
          headingRow.append(heading, clear);
          historySection.appendChild(headingRow);
          history.forEach(item => historySection.appendChild(makeRow(item, '', () => {
            input.value = item;
            saveSearch(item);
            closePanel();
            form.requestSubmit();
          }, '◷')));
          historySection.classList.remove('hidden');
        } else {
          historySection.classList.add('hidden');
        }
        if (currentRequest !== suggestionRequest) return;
        if (input === document.activeElement && (matches.length || history.length)) openPanel();
        else if (!matches.length && !history.length) closePanel();
      };

      input.addEventListener('focus', renderPanel);
      input.addEventListener('input', () => {
        window.clearTimeout(suggestionTimer);
        suggestionTimer = window.setTimeout(renderPanel, 180);
      });
      form.addEventListener('submit', () => {
        saveSearch(input.value);
      });
      document.addEventListener('click', event => {
        if (!form.contains(event.target)) closePanel();
      });
      document.addEventListener('keydown', event => {
        if (event.key === 'Escape') closePanel();
      });
    })();
  </script>
