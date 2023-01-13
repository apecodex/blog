import type {App} from "vue"
import lazy from "./Lazy"

export function setupDirective(app: App<Element>) {
    app.directive("lazy", lazy);
}