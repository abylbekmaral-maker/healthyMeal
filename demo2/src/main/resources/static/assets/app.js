/* Healthy Meal - merged app.js (DB + i18n + stable session) */
window.__APP_LOADED__ = true;
console.log("✅ app.js loaded (merged)");

const HM = (() => {
  const LS = {
    lang: "hm_lang",     // ✅ canonical
    session: "hm_session",
    profile: "hm_profile",
    tracker: "hm_tracker"
  };

  // =========================
  // i18n dictionary (KO/RU/EN)
  // =========================
  const dict = {
    ko: {
      brand_tagline: "영양을 중심으로, 꾸준히",
      brand_story:
        "비타민은 ‘약’보다 ‘음식’으로 섭취했을 때 더 자연스럽게 흡수되는 경우가 많아요. Healthy Meal은 체중보다 ‘영양 흡수’와 ‘컨디션’에 집중합니다.",

      // auth
      login_title: "로그인",
      login_sub: "건강은 ‘덜 먹기’가 아니라 ‘제대로 먹기’에서 시작해요.",
      email: "이메일",
      password: "비밀번호",
      login_btn: "로그인",
      go_login: "로그인 하러가기",
      have_account: "이미 계정이 있으신가요?",
      signup: "회원가입",
      signup_note: "가입 후 프로필에서 목표와 식습관을 설정하면 더 정확한 식단을 추천해 드려요",
      no_account: "계정이 없나요?",
      demo_note: "※ 지금은 화면 완성용(데모) 로그인입니다. 백엔드 연결 시 실제 인증으로 바뀝니다.",

      // profile setup
      setup_title: "건강 프로필 설정",
      setup_sub: "정확한 맞춤 식단을 위해 정보를 입력해주세요",
      age: "나이",
      weight: "체중(kg)",
      height: "키(cm)",
      activity: "활동량",
      activity_ph: "활동량을 선택하세요",
      act_0: "거의 없음 (운동 안함)",
      act_1: "가벼움 (주 1-3일)",
      act_2: "보통 (주 3-5일)",
      act_3: "활동적 (주 6-7일)",
      act_4: "매우 활동적 (하루 2회)",
      goal: "목표 설정",
      g_loss: "체중 감량",
      g_keep: "체중 유지",
      g_gain: "근육 증가",
      diet: "식이 제한 및 알레르기",
      gluten: "글루텐 프리",
      sugar: "무설탕",
      dairy: "유제품 프리",
      veg: "채식주의",
      nuts: "견과류 프리",
      vegan: "비건",
      start: "시작하기",

      // water/tracker misc
      water_title: "물 섭취",
      water_cups_unit: "잔",

      // home
      home_title: "오늘의 식단",
      home_sub: "오늘의 영양 균형",
      direction: "방향",
      dir_balanced: "균형 있는 건강",
      protein: "단백질",
      fiber: "식이섬유",
      meals: "식사 횟수",
      kcal_note: "칼로리(참고용)",
      key_nutrients: "오늘의 주요 영양소",
      key_nutrients_sub: "음식에서 자연스럽게 섭취하는 영양소",
      meals_guide: "오늘 3끼 추천",
      meals_guide_sub: "숫자보다 ‘왜 좋은지’에 집중했어요.",
      why_good: "왜 좋아요?",
      home_empty_title: "식단이 아직 없어요",
      home_empty_default: "Planner에서 주간 플랜을 먼저 생성해 주세요.",

      // planner
      plan_title: "주간 플래너",
      plan_sub: "일주일 식단을 미리 계획하세요",
      ai_btn: "AI 주간 메뉴 생성하기",
      save_plan: "메뉴 저장하기",
      planner_status_loaded: "저장된 주간 플랜을 불러왔어요.",
      planner_status_empty: "아직 저장된 플랜이 없어요. AI로 생성해보세요.",
      planner_status_previewing: "AI가 7일 플랜을 만들고 있어요...",
      planner_status_preview_done: "미리보기 생성 완료! 마음에 들면 저장하세요.",
      planner_status_save_done: "저장 완료!",
      plan_empty_title: "주간 플랜이 아직 없습니다.",
      plan_empty_sub: "AI 버튼을 눌러 주간 메뉴를 생성해보세요.",

      // shopping
      shopping_title: "장보기 리스트",
      shopping_sub: "플랜에 맞춰 미리 준비하세요",
      shopping_empty_sub: "플랜을 생성하면 장보기 리스트가 자동으로 채워져요.",
      shopping_empty_local: "장보기 리스트가 비어있어요.",
      qty_items: "품목",

      // tracker
      tracker_title: "건강 트래커",
      tracker_sub: "영양이 건강에 미치는 영향을 추적하세요",
      sleep: "수면 시간",
      today_sleep: "오늘 수면",
      hours: "시간",
      sleep_msg_good: "✓ 훌륭해요! 권장 수면 시간을 지키고 있어요",
      sleep_react_low: "⚠️ 수면이 부족해요. 오늘은 카페인보다 휴식을 먼저 챙겨요.",
      sleep_react_ok: "괜찮아요. 조금만 더 규칙적으로 자면 컨디션이 더 좋아져요.",
      sleep_react_good: "👍 아주 좋아요! 회복에 도움이 되는 수면 시간이에요.",
      sleep_react_high: "많이 잤어요. 기상 시간을 고정하면 더 개운해질 수 있어요.",
      mood: "기분 & 컨디션",
      mood_q: "오늘 기분은 어떠세요?",
      mood_best: "최고",
      mood_ok: "보통",
      mood_tired: "피곤",
      save_today: "오늘의 데이터 저장하기",
      chart_title: "에너지 vs 영양 점수",
      chart_sub: "식사 품질과 에너지 수준의 상관관계",
      avg_energy: "평균 에너지",
      avg_nutri: "평균 영양",
      corr: "상관도",
      corr_high: "높음",

      // profile
      profile_title: "계정 & 프로필",
      profile_sub: "개인 정보 및 설정 관리",
      personal: "개인 정보",
      name: "이름",
      email_addr: "이메일 주소",
      pw_change: "비밀번호 변경",
      cur_pw: "현재 비밀번호",
      new_pw: "새 비밀번호",
      medical: "의료 정보",
      medical_sub: "맞춤형 식단 계획을 위한 정보",
      health_state: "건강 상태",
      health_state_sub: "영양 섭취에 영향을 줄 수 있는 건강 상태를 선택하세요",
      diabetes: "당뇨",
      hypertension: "고혈압",
      chol: "고콜레스테롤",
      heart: "심장 질환",
      thyroid: "갑상선 질환",
      none: "해당 없음",
      update_med: "의료 정보 업데이트",
      logout: "로그아웃",

      // calendar / summary
      calendar_legend: "색이 있는 날짜는 기록이 있는 날이에요.",
      summary_title: "하루 기록 요약",
      summary_mood_label: "기분",
      summary_sleep_label: "수면",
      summary_water_label: "물",
      record: "아직 이 날은 기록이 없어요.",

      // nav
      nav_home: "홈",
      nav_plan: "계획",
      nav_tracker: "트래커",
      nav_profile: "프로필",

      // nutrition tags -> label
      tag_high_protein: "고단백",
      tag_low_carb: "저탄수",
      tag_high_fiber: "고식이섬유",
      tag_balanced: "균형",
      tag_balance: "균형",
      tag_low_cal: "저칼로리",
      tag_low_fat: "저지방"
    },

    ru: {
      brand_tagline: "Питание — в центре, регулярно",
      brand_story:
        "Витамины часто лучше усваиваются через еду, чем через таблетки. Healthy Meal фокусируется не на весе, а на усвоении нутриентов и самочувствии.",

      login_title: "Вход",
      login_sub: "Здоровье начинается не с «есть меньше», а с «есть правильно».",
      email: "Email",
      password: "Пароль",
      login_btn: "Войти",
      go_login: "Перейти к входу",
      have_account: "У вас уже есть аккаунт?",
      signup: "Регистрация",
      signup_note:
        "После регистрации, если вы укажете цели и привычки в профиле, мы сможем рекомендовать более точный рацион",
      no_account: "Нет аккаунта?",
      demo_note:
        "※ Сейчас это демо-вход для готового дизайна. После подключения бэкенда будет настоящая авторизация.",

      setup_title: "Профиль здоровья",
      setup_sub: "Введите данные для более точного рациона",
      age: "Возраст",
      weight: "Вес (кг)",
      height: "Рост (см)",
      activity: "Активность",
      activity_ph: "Выберите активность",
      act_0: "Почти нет (без спорта)",
      act_1: "Лёгкая (1–3 раза/нед)",
      act_2: "Средняя (3–5 раз/нед)",
      act_3: "Высокая (6–7 раз/нед)",
      act_4: "Очень высокая (2 раза/день)",
      goal: "Цель",
      g_loss: "Снижение веса",
      g_keep: "Поддержание",
      g_gain: "Набор мышц",
      diet: "Ограничения и аллергии",
      gluten: "Без глютена",
      sugar: "Без сахара",
      dairy: "Без молочного",
      veg: "Вегетарианство",
      nuts: "Без орехов",
      vegan: "Веган",
      start: "Начать",

      water_title: "Вода",
      water_cups_unit: "стакан(ов)",

      home_title: "Рацион на сегодня",
      home_sub: "Баланс нутриентов сегодня",
      direction: "Направление",
      dir_balanced: "Сбалансированное здоровье",
      protein: "Белок",
      fiber: "Клетчатка",
      meals: "Приёмы пищи",
      kcal_note: "Калории (справочно)",
      key_nutrients: "Ключевые нутриенты",
      key_nutrients_sub: "То, что естественно получаем из еды",
      meals_guide: "3 приёма пищи на сегодня",
      meals_guide_sub: "Упор на пользу, а не только на цифры.",
      why_good: "Почему это хорошо?",
      home_empty_title: "Рациона ещё нет",
      home_empty_default: "Сначала создайте недельный план в Planner.",

      plan_title: "Недельный план",
      plan_sub: "Запланируйте питание на неделю заранее",
      ai_btn: "Сгенерировать меню на неделю (AI)",
      save_plan: "Сохранить меню",
      planner_status_loaded: "Загружен сохранённый недельный план.",
      planner_status_empty: "Сохранённого плана нет. Создайте через AI.",
      planner_status_previewing: "AI создаёт план на 7 дней...",
      planner_status_preview_done: "Превью готово! Если нравится — сохраните.",
      planner_status_save_done: "Сохранено!",
      plan_empty_title: "Недельного плана ещё нет.",
      plan_empty_sub: "Нажмите AI, чтобы сгенерировать меню на неделю.",

      shopping_title: "Список покупок",
      shopping_sub: "Подготовьтесь заранее по плану",
      shopping_empty_sub: "После создания плана список покупок заполнится автоматически.",
      shopping_empty_local: "Список покупок пуст.",
      qty_items: "позиций",

      tracker_title: "Трекер здоровья",
      tracker_sub: "Отслеживайте влияние питания на самочувствие",
      sleep: "Сон",
      today_sleep: "Сегодняшний сон",
      hours: "часов",
      sleep_msg_good: "✓ Отлично! Вы держите рекомендуемую норму сна",
      sleep_react_low: "⚠️ Мало сна. Лучше сделать упор на отдых, а не на кофеин.",
      sleep_react_ok: "Неплохо. Если чуть регулярнее — самочувствие станет лучше.",
      sleep_react_good: "👍 Отлично! Это хороший диапазон для восстановления.",
      sleep_react_high: "Много сна. Фиксированное время подъёма поможет бодрее.",
      mood: "Настроение и состояние",
      mood_q: "Какое у вас настроение сегодня?",
      mood_best: "Отлично",
      mood_ok: "Нормально",
      mood_tired: "Устал(а)",
      save_today: "Сохранить данные за сегодня",
      chart_title: "Энергия vs питание",
      chart_sub: "Связь качества еды и уровня энергии",
      avg_energy: "Средняя энергия",
      avg_nutri: "Среднее питание",
      corr: "Корреляция",
      corr_high: "Высокая",

      profile_title: "Аккаунт и профиль",
      profile_sub: "Управление личными данными и настройками",
      personal: "Личные данные",
      name: "Имя",
      email_addr: "Email",
      pw_change: "Сменить пароль",
      cur_pw: "Текущий пароль",
      new_pw: "Новый пароль",
      medical: "Медицинская информация",
      medical_sub: "Для персонального планирования рациона",
      health_state: "Состояние здоровья",
      health_state_sub: "Выберите состояния, которые могут влиять на питание",
      diabetes: "Диабет",
      hypertension: "Гипертония",
      chol: "Высокий холестерин",
      heart: "Болезни сердца",
      thyroid: "Щитовидная железа",
      none: "Нет",
      update_med: "Обновить мед. данные",
      logout: "Выйти",

      calendar_legend: "Выделенные цветом даты — это дни с записями.",
      summary_title: "Краткий итог дня",
      summary_mood_label: "Настроение",
      summary_sleep_label: "Сон",
      summary_water_label: "Вода",
      record: "В этот день записей нет.",

      nav_home: "Дом",
      nav_plan: "План",
      nav_tracker: "Трекер",
      nav_profile: "Профиль",

      tag_high_protein: "Высокий белок",
      tag_low_carb: "Низкие углеводы",
      tag_high_fiber: "Много клетчатки",
      tag_balanced: "Сбалансировано",
      tag_balance: "Сбалансировано",
      tag_low_cal: "Низкокалорийно",
      tag_low_fat: "Мало жира"
    },

    en: {
      brand_tagline: "Nutrition first, consistently",
      brand_story:
        "Vitamins are often absorbed more naturally through food than pills. Healthy Meal focuses on nutrient absorption and well-being, not just weight.",

      login_title: "Login",
      login_sub: "Health starts not with “eat less” but with “eat right.”",
      email: "Email",
      password: "Password",
      login_btn: "Login",
      go_login: "Go to Login",
      have_account: "Already have an account?",
      signup: "Sign up",
      signup_note:
        "After signing up, set your health goals and habits in your profile for more accurate recommendations.",
      no_account: "No account?",
      demo_note:
        "※ This is a design-only demo login. It will become real authentication after backend integration.",

      setup_title: "Health Profile Setup",
      setup_sub: "Enter info for a more accurate meal plan",
      age: "Age",
      weight: "Weight (kg)",
      height: "Height (cm)",
      activity: "Activity",
      activity_ph: "Select activity level",
      act_0: "Almost none",
      act_1: "Light (1–3 days/week)",
      act_2: "Moderate (3–5 days/week)",
      act_3: "Active (6–7 days/week)",
      act_4: "Very active (twice/day)",
      goal: "Goal",
      g_loss: "Lose weight",
      g_keep: "Maintain",
      g_gain: "Build muscle",
      diet: "Dietary limits & allergies",
      gluten: "Gluten-free",
      sugar: "Sugar-free",
      dairy: "Dairy-free",
      veg: "Vegetarian",
      nuts: "Nut-free",
      vegan: "Vegan",
      start: "Get started",

      water_title: "Water Intake",
      water_cups_unit: "cups",

      home_title: "Today's Meals",
      home_sub: "Today's nutrition balance",
      direction: "Focus",
      dir_balanced: "Balanced health",
      protein: "Protein",
      fiber: "Fiber",
      meals: "Meals",
      kcal_note: "Calories (ref.)",
      key_nutrients: "Key nutrients today",
      key_nutrients_sub: "Naturally obtained from food",
      meals_guide: "3 meals for today",
      meals_guide_sub: "We focus on “why it’s good”, not just numbers.",
      why_good: "Why is it good?",
      home_empty_title: "No meals yet",
      home_empty_default: "Create a weekly plan in Planner first.",

      plan_title: "Weekly Planner",
      plan_sub: "Plan your meals for the week ahead",
      ai_btn: "Generate weekly menu (AI)",
      save_plan: "Save menu",
      planner_status_loaded: "Loaded your saved weekly plan.",
      planner_status_empty: "No saved plan. Generate one with AI.",
      planner_status_previewing: "AI is generating a 7-day plan...",
      planner_status_preview_done: "Preview ready! Save it if you like it.",
      planner_status_save_done: "Saved!",
      plan_empty_title: "No weekly plan yet.",
      plan_empty_sub: "Tap AI to generate your weekly menu.",

      shopping_title: "Shopping list",
      shopping_sub: "Prepare ahead based on your plan",
      shopping_empty_sub: "Once you generate a plan, the shopping list will auto-fill.",
      shopping_empty_local: "Shopping list is empty.",
      qty_items: "items",

      tracker_title: "Health Tracker",
      tracker_sub: "Track how nutrition affects your health",
      sleep: "Sleep",
      today_sleep: "Today's sleep",
      hours: "hours",
      sleep_msg_good: "✓ Great! You’re meeting the recommended sleep time",
      sleep_react_low: "⚠️ Not enough sleep. Prioritize rest today over extra caffeine.",
      sleep_react_ok: "Not bad. A bit more consistency will improve your energy.",
      sleep_react_good: "👍 Great! This is a solid range for recovery.",
      sleep_react_high: "Slept a lot. A fixed wake-up time may help you feel fresher.",
      mood: "Mood & condition",
      mood_q: "How do you feel today?",
      mood_best: "Great",
      mood_ok: "Okay",
      mood_tired: "Tired",
      save_today: "Save today's data",
      chart_title: "Energy vs Nutrition Score",
      chart_sub: "Correlation between food quality and energy level",
      avg_energy: "Avg energy",
      avg_nutri: "Avg nutrition",
      corr: "Correlation",
      corr_high: "High",

      profile_title: "Account & Profile",
      profile_sub: "Manage personal info and settings",
      personal: "Personal info",
      name: "Name",
      email_addr: "Email address",
      pw_change: "Change password",
      cur_pw: "Current password",
      new_pw: "New password",
      medical: "Medical info",
      medical_sub: "For personalized meal planning",
      health_state: "Health conditions",
      health_state_sub: "Select conditions that may affect nutrition",
      diabetes: "Diabetes",
      hypertension: "Hypertension",
      chol: "High cholesterol",
      heart: "Heart disease",
      thyroid: "Thyroid issues",
      none: "None",
      update_med: "Update medical info",
      logout: "Log out",

      calendar_legend: "Colored dates are days with records.",
      summary_title: "Daily summary",
      summary_mood_label: "Mood",
      summary_sleep_label: "Sleep",
      summary_water_label: "Water",
      record: "No records for this day.",

      nav_home: "Home",
      nav_plan: "Plan",
      nav_tracker: "Tracker",
      nav_profile: "Profile",

      tag_high_protein: "High protein",
      tag_low_carb: "Low carb",
      tag_high_fiber: "High fiber",
      tag_balanced: "Balanced",
      tag_balance: "Balanced",
      tag_low_cal: "Low calorie",
      tag_low_fat: "Low fat"
    }
  };

  function normalizeLang(raw) {
    const v = (raw || "").toString().trim().toLowerCase();
    if (v === "ru") return "ru";
    if (v === "en") return "en";
    return "ko";
  }

  function getLang() {
    return normalizeLang(
      localStorage.getItem(LS.lang) || localStorage.getItem("lang") || "ko"
    );
  }

  function setLang(lang) {
    const n = normalizeLang(lang);
    localStorage.setItem(LS.lang, n);
    localStorage.setItem("lang", n); // legacy compatibility
    applyI18n();
  }

  function t(key) {
    const lang = getLang();
    return (dict[lang] && dict[lang][key]) || (dict.ko[key] || key);
  }

  // -------------------------
  // Tag label helper
  // -------------------------
  function normalizeTag(raw) {
    if (!raw) return "";
    let s = String(raw).trim();
    if (!s) return "";
    s = s.toLowerCase().replaceAll("-", "_");

    if (s === "balance") s = "balanced";
    if (s === "highprotein") s = "high_protein";
    if (s === "lowcarb") s = "low_carb";
    if (s === "highfiber") s = "high_fiber";
    if (s === "lowcal") s = "low_cal";
    if (s === "lowfat") s = "low_fat";

    return s;
  }

  function tagLabel(raw) {
    const n = normalizeTag(raw);
    if (!n) return "";
    const key = "tag_" + n;
    const out = t(key);
    return out || raw;
  }

  // -------------------------
  // i18n apply
  // -------------------------
  function applyI18n() {
    const lang = getLang();
    document.documentElement.setAttribute("lang", lang);

    document.querySelectorAll("[data-set-lang]").forEach(btn => {
      btn.classList.toggle("active", btn.dataset.setLang === lang);
    });

    document.querySelectorAll("[data-i18n]").forEach(el => {
      const key = el.dataset.i18n;
      el.textContent = t(key);
    });

    document.querySelectorAll("[data-i18n-ph]").forEach(el => {
      const key = el.dataset.i18nPh;
      el.setAttribute("placeholder", t(key));
    });

    document.querySelectorAll("[data-i18n-opt]").forEach(el => {
      const key = el.dataset.i18nOpt;
      el.textContent = t(key);
    });
  }

  function setActiveNav(id) {
    document.querySelectorAll(".nav-item").forEach(a => {
      a.classList.toggle("active", a.dataset.nav === id);
    });
  }

  /**
   * ✅ Session rule (single source of truth):
   * - login/signup page: skip
   * - needs userId, and profile setup needs hasProfile === "true"
   */
  function ensureSession() {
    try {
      const page = document.body?.dataset?.page;

      // allow unauth pages
      if (page === "login" || page === "signup") return true;

      const userId = localStorage.getItem("userId");
      if (!userId) {
        window.location.href = "./login.html";
        return false;
      }

      const hasProfile = localStorage.getItem("hasProfile");
      if (hasProfile !== "true" && page !== "profile-setup") {
        window.location.href = "./profile-setup.html";
        return false;
      }

      return true;
    } catch (e) {
      console.error(e);
      window.location.href = "./login.html";
      return false;
    }
  }

  function logout() {
    localStorage.removeItem("userId");
    localStorage.removeItem("hasProfile");
    window.location.href = "./login.html";
  }

  function wire() {
    ensureSession();

    document.querySelectorAll("[data-set-lang]").forEach(btn => {
      btn.addEventListener("click", () => setLang(btn.dataset.setLang));
    });

    applyI18n();
  }

  return {
    wire,
    applyI18n,
    setActiveNav,
    setLang,
    getLang,
    t,
    tagLabel,
    ensureSession,
    logout
  };
})();

window.HM = HM;

document.addEventListener("DOMContentLoaded", () => {
  if (window.HM && typeof window.HM.wire === "function") {
    window.HM.wire();
  }
});
