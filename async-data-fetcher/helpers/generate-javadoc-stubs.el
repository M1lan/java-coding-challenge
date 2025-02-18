;;; generate-javadoc-stubs.el --- GNU/Emacs -*- lexical-binding: t; -*-

;;; Commentary:
;; Custom Emacs setup for this coding challenge: to make sure everything
;; is documented we add Javadoc stubs everywhere!
;;
;; At the end, we can change the stubs into actual Javadoc descriptions.

;;;
;; Author: Milan Santosi February 16, 2025. MIT License.

;;; Code:
;;
(defun insert-javadoc-stub ()
  "Insert a stub Javadoc comment above methods in the current buffer.
It searches for lines starting with public, protected, or private that
contain a `(' and a `{'.  If no Javadoc comment is already present
immediately above, it inserts a stub."
  (save-excursion
    (goto-char (point-min))
    (while
	  (re-search-forward "^[[:space:]]*\\(public\\|protected\\|private\\)[^{;]*([^{;]*{" nil t)
      (let ((method-start (match-beginning 0)))
        (save-excursion
          (goto-char method-start)
          (forward-line -1)
          (unless (or (looking-at-p "^[[:space:]]*/\\*\\*")  ; already a Javadoc comment?
                    (looking-at-p "^[[:space:]]*//"))        ; or a single-line comment
            (goto-char method-start)
            (insert "/**\n * TODO: Auto-generated Javadoc\n */\n")))))))

(defun add-javadoc-stubs-to-project (directory)
  "Recursively add stub Javadoc comments to all Java files in DIRECTORY.
For each .java file found, open the file, insert Javadoc stubs where
needed, save the file, and close it."
  (interactive "DSelect project directory: ")
  (let ((files (directory-files-recursively directory "\\.java$")))
    (dolist (file files)
      (with-current-buffer (find-file-noselect file)
        (message "Processing %s" file)
        (insert-javadoc-stub)
        (save-buffer)
        (kill-buffer)))))

;; For Emacs newbies: To load this code, evaluate the buffer (M-x
;; eval-buffer) or save it as generate-javadoc-stubs.el and load it
;; with: M-x load-file RET /path/to/generate-javadoc-stubs.el RET


(provide 'generate-javadoc-stubs)
;;; generate-javadoc-stubs.el ends here.
