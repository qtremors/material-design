const repositoryApi = "https://api.github.com/repos/qtremors/material-design";
const numberFormatter = new Intl.NumberFormat();
const reducedMotion = window.matchMedia("(prefers-reduced-motion: reduce)");

function setText(id, value) {
  const element = document.getElementById(id);
  if (element) element.textContent = value;
}

function animateNumber(id, value) {
  const element = document.getElementById(id);
  if (!element || !Number.isFinite(value)) return;

  const target = Math.max(0, Math.trunc(value));
  if (reducedMotion.matches) {
    element.textContent = numberFormatter.format(target);
    return;
  }

  const startedAt = performance.now();
  const duration = 700;

  function update(now) {
    const progress = Math.min((now - startedAt) / duration, 1);
    const eased = 1 - Math.pow(1 - progress, 3);
    element.textContent = numberFormatter.format(Math.round(target * eased));
    if (progress < 1) requestAnimationFrame(update);
  }

  requestAnimationFrame(update);
}

function releaseDownloadCount(releases) {
  return releases.reduce((releaseTotal, release) => {
    const assets = Array.isArray(release.assets) ? release.assets : [];
    return releaseTotal + assets.reduce((assetTotal, asset) => assetTotal + (asset.download_count || 0), 0);
  }, 0);
}

async function loadGitHubStats() {
  if (!document.getElementById("gh-stars")) return;

  try {
    const [repositoryResult, releasesResult] = await Promise.allSettled([
      fetch(repositoryApi, { headers: { Accept: "application/vnd.github+json" } }),
      fetch(`${repositoryApi}/releases?per_page=100`, { headers: { Accept: "application/vnd.github+json" } }),
    ]);

    if (repositoryResult.status === "fulfilled" && repositoryResult.value.ok) {
      const repository = await repositoryResult.value.json();
      animateNumber("gh-stars", repository.stargazers_count || 0);
      animateNumber("gh-forks", repository.forks_count || 0);
    } else {
      setText("gh-stars", "1");
      setText("gh-forks", "1");
    }

    if (releasesResult.status === "fulfilled" && releasesResult.value.ok) {
      const releases = await releasesResult.value.json();
      if (Array.isArray(releases) && releases.length > 0) {
        const total = releaseDownloadCount(releases);
        const latest = releases[0];
        const latestCount = releaseDownloadCount([latest]);
        const releaseName = latest.name || latest.tag_name;

        animateNumber("gh-total-downloads", total);
        animateNumber("gh-latest-downloads", latestCount);

        if (releaseName) {
          document.querySelectorAll(".download-button-text").forEach((element) => {
            element.textContent = `Download ${releaseName}`;
          });
        }
      } else {
        animateNumber("gh-total-downloads", 0);
        animateNumber("gh-latest-downloads", 0);
      }
    } else {
      setText("gh-total-downloads", "0");
      setText("gh-latest-downloads", "0");
    }
  } catch (err) {
    setText("gh-stars", "1");
    setText("gh-forks", "1");
    setText("gh-total-downloads", "0");
    setText("gh-latest-downloads", "0");
  }
}

function installMobileNavigation() {
  const header = document.querySelector(".site-header");
  const navigation = header?.querySelector("nav");
  if (!header || !navigation || header.querySelector(".mobile-menu-button")) return;

  const button = document.createElement("button");
  button.className = "mobile-menu-button";
  button.type = "button";
  button.setAttribute("aria-label", "Open navigation");
  button.setAttribute("aria-expanded", "false");
  button.innerHTML = '<span class="material-symbols-rounded" aria-hidden="true">menu</span>';

  const menu = document.createElement("div");
  menu.className = "mobile-menu";
  menu.id = "mobile-navigation";
  menu.setAttribute("role", "dialog");
  menu.setAttribute("aria-label", "Site navigation");
  menu.setAttribute("aria-modal", "true");
  menu.setAttribute("aria-hidden", "true");
  menu.inert = true;

  const menuNavigation = document.createElement("nav");
  menuNavigation.setAttribute("aria-label", "Mobile navigation");
  navigation.querySelectorAll("a").forEach((link) => {
    const clone = link.cloneNode(true);
    clone.classList.remove("nav-action");
    menuNavigation.appendChild(clone);
  });
  menu.appendChild(menuNavigation);
  button.setAttribute("aria-controls", menu.id);
  header.appendChild(button);
  document.body.appendChild(menu);

  function closeMenu(restoreFocus = false) {
    menu.classList.remove("open");
    menu.setAttribute("aria-hidden", "true");
    menu.inert = true;
    button.setAttribute("aria-expanded", "false");
    button.setAttribute("aria-label", "Open navigation");
    button.querySelector(".material-symbols-rounded").textContent = "menu";
    document.body.classList.remove("mobile-menu-open");
    if (restoreFocus) button.focus();
  }

  function openMenu() {
    menu.classList.add("open");
    menu.setAttribute("aria-hidden", "false");
    menu.inert = false;
    button.setAttribute("aria-expanded", "true");
    button.setAttribute("aria-label", "Close navigation");
    button.querySelector(".material-symbols-rounded").textContent = "close";
    document.body.classList.add("mobile-menu-open");
    menu.querySelector("a")?.focus();
  }

  button.addEventListener("click", () => {
    if (button.getAttribute("aria-expanded") === "true") closeMenu();
    else openMenu();
  });

  menu.addEventListener("click", (event) => {
    if (event.target.closest("a")) closeMenu();
  });

  document.addEventListener("keydown", (event) => {
    if (button.getAttribute("aria-expanded") !== "true") return;
    if (event.key === "Escape") {
      closeMenu(true);
      return;
    }
    if (event.key === "Tab") {
      const focusable = [button, ...menu.querySelectorAll("a")];
      const currentIndex = focusable.indexOf(document.activeElement);
      const direction = event.shiftKey ? -1 : 1;
      const nextIndex = currentIndex < 0
        ? (event.shiftKey ? focusable.length - 1 : 0)
        : (currentIndex + direction + focusable.length) % focusable.length;
      event.preventDefault();
      focusable[nextIndex].focus();
    }
  });

  window.matchMedia("(min-width: 901px)").addEventListener("change", (event) => {
    if (event.matches) closeMenu();
  });
}

document.addEventListener("DOMContentLoaded", () => {
  installMobileNavigation();
  loadGitHubStats().catch(() => {
    setText("gh-stars", "--");
    setText("gh-forks", "--");
    setText("gh-total-downloads", "--");
    setText("gh-latest-downloads", "--");
  });
});
