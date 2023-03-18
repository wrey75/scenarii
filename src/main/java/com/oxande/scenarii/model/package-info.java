/**
 * This package stores the model for the database.
 *
 * <p>
 *     A film is composed of scenes and dialogs. To have a very simplified way to work, the scene are included
 *     in the film like other things. Basically, the {@link com.oxande.scenarii.model.Paragraph} entity
 *     will include everything (dialog, action, scene, title and so on). This is a very basic way to
 *     achieve the screenplay. Note each paragraph is stored when changed in
 *     {@link com.oxande.scenarii.model.ParagraphHistory} which has a link the the paragraph itself.
 * </p>
 *
 * <p>
 *     Due to JPA, I decided to have an {@link com.oxande.scenarii.model.AbstractParagraph} which is
 *     a perfect match with {@link com.oxande.scenarii.model.Paragraph} itself. But this is a simplification
 *     to keep {@link com.oxande.scenarii.model.ParagraphHistory} a table. The idea behind this is to
 *     keep awy the history of the a paragraph (all the changes done to it) from the final version.
 * </p>
 */
package com.oxande.scenarii.model;